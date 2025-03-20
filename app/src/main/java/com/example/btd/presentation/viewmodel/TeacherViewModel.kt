package com.example.btd.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btd.data.models.AbsenceModel
import com.example.btd.data.models.StudentAbsenceModel
import com.example.btd.domain.converters.GetStudentAbsencesConverter
import com.example.btd.domain.converters.GetStudentConverter
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.GetStudentAbsencesUseCase
import com.example.btd.domain.use_cases.GetStudentUseCase
import com.example.btd.presentation.models.AttendanceRecord
import com.example.btd.presentation.models.StudentAbsence
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed class TeacherVerificationState {
    object Loading : TeacherVerificationState()
    object Verified : TeacherVerificationState()
    object NotVerified : TeacherVerificationState()
}

class TeacherViewModel : ViewModel() {
    private val getStudent = GetStudentUseCase()
    private val getStudentAbsences = GetStudentAbsencesUseCase()
    private val studentConverter = GetStudentConverter()
    private val absencesConverter = GetStudentAbsencesConverter()

    private val _attendanceRecords = MutableStateFlow<List<AttendanceRecord>>(emptyList())
    val attendanceRecords: StateFlow<List<AttendanceRecord>> = _attendanceRecords

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _verificationState =
        MutableStateFlow<TeacherVerificationState>(TeacherVerificationState.Loading)
    val verificationState: StateFlow<TeacherVerificationState> = _verificationState

    init {
        loadAttendanceData()
    }

    fun refreshAttendanceData() {
        loadAttendanceData()
    }

    private fun loadAttendanceData() {
        viewModelScope.launch {
             try {
                 _isRefreshing.value = true
                 val now = LocalDate.now()
                 val studentRequest = GetStudentUseCase.Request(
                     year = now.year,
                     month = now.monthValue
                 )

                 try {
                     val studentResult = getStudent.execute(studentRequest)
                         .map { studentConverter.convert(it) }
                         .first()

                     when (studentResult) {
                         is UiState.Success -> {
                             _verificationState.value = TeacherVerificationState.Verified

                             val studentsList = studentResult.data
                             if (studentsList.isEmpty()) {
                                 Log.d("TeacherViewModel", "No students found")
                                 _attendanceRecords.value = emptyList()
                             } else {
                                 val allRecords = mutableListOf<AttendanceRecord>()

                                 for (student in studentsList) {
                                     val absencesRequest = GetStudentAbsencesUseCase.Request(
                                         studentId = student.studentId,
                                         year = now.year,
                                         month = now.monthValue
                                     )

                                     try {
                                         val absencesResult =
                                             getStudentAbsences.execute(absencesRequest)
                                                 .map { absencesConverter.convert(it) }
                                                 .first()

                                         when (absencesResult) {
                                             is UiState.Success -> {
                                                 val records = convertToAttendanceRecords(
                                                     student,
                                                     absencesResult.data
                                                 )
                                                 allRecords.addAll(records)
                                             }

                                             is UiState.Error -> {
                                                 val errorMsg = absencesResult.errorMessage
                                                 if (isAuthenticationError(errorMsg)) {
                                                     _verificationState.value =
                                                         TeacherVerificationState.NotVerified
                                                     return@launch
                                                 } else {
                                                     Log.e(
                                                         "TeacherViewModel",
                                                         "Error getting absences for student ${student.studentId}: $errorMsg"
                                                     )
                                                 }
                                             }

                                             else -> {}
                                         }
                                     } catch (e: Exception) {
                                         Log.e(
                                             "TeacherViewModel",
                                             "Exception getting absences for student ${student.studentId}: ${e.message}"
                                         )
                                     }
                                 }


                                 val mergedRecords = mergeRecordsByDate(allRecords)
                                 _attendanceRecords.value = mergedRecords
                             }
                         }

                         is UiState.Error -> {
                             val errorMsg = studentResult.errorMessage
                             if (isAuthenticationError(errorMsg)) {
                                 _verificationState.value = TeacherVerificationState.NotVerified
                             } else {
                                 Log.e("TeacherViewModel", "Error loading students: $errorMsg")
                                 _verificationState.value = TeacherVerificationState.Verified
                             }
                         }

                         else -> {}
                     }
                 } catch (e: Exception) {
                     val errorMsg = e.message ?: e.toString()
                     Log.e("TeacherViewModel", "Exception during data loading: $errorMsg")

                     if (isAuthenticationError(errorMsg)) {
                         _verificationState.value = TeacherVerificationState.NotVerified
                     } else {
                         _verificationState.value = TeacherVerificationState.Verified
                     }
                 }
             } finally {
                 _isRefreshing.value = false
             }
        }
    }

    private fun mergeRecordsByDate(records: List<AttendanceRecord>): List<AttendanceRecord> {
        val recordMap = mutableMapOf<LocalDate, MutableList<StudentAbsence>>()

        for (record in records) {
            val date = record.date
            val existingAbsences = recordMap.getOrPut(date) { mutableListOf() }
            existingAbsences.addAll(record.absences)
        }

        return recordMap.map { (date, absences) ->
            AttendanceRecord(
                date = date,
                absences = absences
            )
        }
    }

    private fun isAuthenticationError(errorMessage: String): Boolean {
        return errorMessage.contains("BAD_AUTHENTICATION", ignoreCase = true) ||
                errorMessage.contains("Long live credential not available", ignoreCase = true) ||
                errorMessage.contains("permission", ignoreCase = true) ||
                errorMessage.contains("unauthorized", ignoreCase = true) ||
                errorMessage.contains("authentication", ignoreCase = true) ||
                errorMessage.contains("auth", ignoreCase = true) ||
                errorMessage.contains("credential", ignoreCase = true) ||
                errorMessage.contains("token", ignoreCase = true) ||
                errorMessage.contains("verify", ignoreCase = true)
    }

    private fun convertToAttendanceRecords(
        student: StudentAbsenceModel,
        absences: List<AbsenceModel>,
    ): List<AttendanceRecord> {
        val groupName = if (student.groups.isNotEmpty()) student.groups[0].number else "No Group"
        val fullName = "${student.surname} ${student.name}"

        val groupedByDate = absences.flatMap { absence ->
            val startDate = try {
                LocalDate.parse(absence.from.substringBefore('T'))
            } catch (e: Exception) {
                LocalDate.now()
            }

            val endDate = try {
                LocalDate.parse(absence.to.substringBefore('T'))
            } catch (e: Exception) {
                startDate
            }

            var current = startDate
            val dates = mutableListOf<LocalDate>()
            while (!current.isAfter(endDate)) {
                dates.add(current)
                current = current.plusDays(1)
            }

            dates.map { date -> date to absence }
        }.groupBy({ it.first }, { it.second })

        return groupedByDate.map { (date, absencesForDate) ->
            AttendanceRecord(
                date = date,
                absences = absencesForDate.map { absence ->
                    StudentAbsence(
                        id = absence.id,
                        name = fullName,
                        hasProof = absence.files.isNotEmpty(),
                        group = groupName
                    )
                }
            )
        }
    }
}