package com.example.btd.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btd.data.models.AbsenceModel
import com.example.btd.domain.converters.AddAllAbsenceConverter
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.AddAllAbsenceUseCase
import com.example.btd.services.MockStudentAbsenceService
import com.example.btd.services.StudentAbsenceService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

sealed class SubmissionResult {
    object Success : SubmissionResult()
    data class Error(val message: String) : SubmissionResult()
}

class StudentScreenViewModel : ViewModel() {
    private val getAllAbsents = AddAllAbsenceUseCase()
    private val addAllAbsenceConverter = AddAllAbsenceConverter()

    private val service: StudentAbsenceService = MockStudentAbsenceService

    private val _submissions = MutableStateFlow<List<AbsenceModel>>(emptyList())
    val submissions: StateFlow<List<AbsenceModel>> get() = _submissions


    private val _submissionResult = MutableSharedFlow<SubmissionResult>()
    val submissionResult: SharedFlow<SubmissionResult> = _submissionResult

    init {
        loadSubmissions()
    }

    fun loadSubmissions() {
        viewModelScope.launch {
            getAllAbsents.execute(
                AddAllAbsenceUseCase.Request(null, null, year = 2025, month = 3)
            ).map {
                addAllAbsenceConverter.convert(it)
            }.collect {
                if (it is UiState.Success)
                    _submissions.value = it.data
                if(it is UiState.Error)
                    Log.d("ssvm","error: ${it.errorMessage}")
            }

        }
    }

    fun refreshSubmissions() {
        viewModelScope.launch {
            getAllAbsents.execute(
                AddAllAbsenceUseCase.Request(null, null, year = 2025, month = 3)
            ).map {
                addAllAbsenceConverter.convert(it)
            }.collect {
                if (it is UiState.Success)
                    _submissions.value = it.data
                if(it is UiState.Error)
                    Log.d("ssvm","error: ${it.errorMessage}")
            }
        }
    }

    fun submitAbsence(
        startDate: LocalDate,
        endDate: LocalDate,
        reason: String,
        document: List<MultipartBody.Part>,
    ) {
        viewModelScope.launch {

            when {
                startDate.isAfter(endDate) -> {
                    _submissionResult.emit(SubmissionResult.Error("Start date cannot be after end date"))
                }
//                startDate.isBefore(LocalDate.now()) -> {
//                    _submissionResult.emit(SubmissionResult.Error("Start date cannot be in the past"))
//                }
//                endDate.isBefore(LocalDate.now()) -> {
//                    _submissionResult.emit(SubmissionResult.Error("End date cannot be in the past"))
//                }
                else -> {
                    try {
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                       service.submitAbsence(
                            startDate.atStartOfDay(ZoneOffset.UTC).format(formatter),
                            endDate.atStartOfDay(ZoneOffset.UTC).format(formatter),
                            reason,
                            "fucking name",
                            document
                        )
                        loadSubmissions()
                        _submissionResult.emit(SubmissionResult.Success)
                    } catch (e: Exception) {
                        _submissionResult.emit(SubmissionResult.Error("Failed to submit absence: ${e.message}"))
                    }
                }
            }
        }
    }
}