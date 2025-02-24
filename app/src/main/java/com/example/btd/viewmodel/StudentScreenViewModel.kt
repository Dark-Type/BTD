package com.example.btd.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btd.services.AbsenceSubmission
import com.example.btd.services.MockStudentAbsenceService
import com.example.btd.services.StudentAbsenceService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed class SubmissionResult {
    object Success : SubmissionResult()
    data class Error(val message: String) : SubmissionResult()
}

class StudentScreenViewModel : ViewModel() {
    private val service: StudentAbsenceService = MockStudentAbsenceService

    private val _submissions = MutableStateFlow<List<AbsenceSubmission>>(emptyList())
    val submissions: StateFlow<List<AbsenceSubmission>> get() = _submissions


    private val _submissionResult = MutableSharedFlow<SubmissionResult>()
    val submissionResult: SharedFlow<SubmissionResult> = _submissionResult

    init {
        loadSubmissions()
    }

    fun loadSubmissions() {
        viewModelScope.launch {
            _submissions.value = service.getSubmissions()
        }
    }

    fun refreshSubmissions() {
        viewModelScope.launch {
            _submissions.value = service.refreshStatuses(_submissions.value)
        }
    }

    fun submitAbsence(startDate: LocalDate, endDate: LocalDate, document: String?) {
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
                        service.submitAbsence(startDate, endDate, document)
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