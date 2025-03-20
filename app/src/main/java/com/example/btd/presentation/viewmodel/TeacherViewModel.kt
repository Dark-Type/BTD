package com.example.btd.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btd.data.models.AbsenceStatus
import com.example.btd.data.models.StudentAbsenceModel
import com.example.btd.domain.converters.GetStudentConverter
import com.example.btd.domain.models.UiState
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
    private val getStudentAbsences = GetStudentUseCase()
    private val absencesConverter = GetStudentConverter()

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


                val absencesRequest = GetStudentUseCase.Request(

                    year = now.year,
                    month = now.monthValue
                )

                try {
                    val absencesResult = getStudentAbsences.execute(absencesRequest)
                        .map { absencesConverter.convert(it) }
                        .first()

                    when (absencesResult) {
                        is UiState.Success -> {
                            _verificationState.value = TeacherVerificationState.Verified
                            val studentAbsences = absencesResult.data

                            if (studentAbsences.isEmpty()) {
                                Log.d("TeacherViewModel", "No student absences found")
                                _attendanceRecords.value = emptyList()
                            } else {
                                val records = convertToAttendanceRecords(studentAbsences)
                                _attendanceRecords.value = records
                            }
                        }

                        is UiState.Error -> {
                            val errorMsg = absencesResult.errorMessage
                            if (isAuthenticationError(errorMsg)) {
                                _verificationState.value = TeacherVerificationState.NotVerified
                            } else {
                                Log.e(
                                    "TeacherViewModel",
                                    "Error loading student absences: $errorMsg"
                                )
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

    private fun isAuthenticationError(errorMessage: String): Boolean {
        return errorMessage.contains("500", ignoreCase = true) ||
                errorMessage.contains("permission", ignoreCase = true) ||
                errorMessage.contains("unauthorized", ignoreCase = true) ||
                errorMessage.contains("authentication", ignoreCase = true) ||
                errorMessage.contains("auth", ignoreCase = true) ||
                errorMessage.contains("credential", ignoreCase = true) ||
                errorMessage.contains("token", ignoreCase = true) ||
                errorMessage.contains("verify", ignoreCase = true)
    }

    private fun convertToAttendanceRecords(studentAbsences: List<StudentAbsenceModel>): List<AttendanceRecord> {
        val recordsMap = mutableMapOf<LocalDate, MutableList<StudentAbsence>>()

        for (student in studentAbsences) {
            val groupName =
                if (student.groups.isNotEmpty()) student.groups[0].number else "No Group"
            val fullName = "${student.surname} ${student.name}"

            for (absence in student.absences) {
                if (absence.status == AbsenceStatus.Checking) {
                    continue
                }

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
                while (!current.isAfter(endDate)) {
                    val absencesForDate = recordsMap.getOrPut(current) { mutableListOf() }

                    absencesForDate.add(
                        StudentAbsence(
                            id = absence.id,
                            name = fullName,
                            hasProof = absence.status == AbsenceStatus.Approved,
                            group = groupName
                        )
                    )

                    current = current.plusDays(1)
                }
            }
        }

        return recordsMap.map { (date, absences) ->
            AttendanceRecord(
                date = date,
                absences = absences
            )
        }
    }
}