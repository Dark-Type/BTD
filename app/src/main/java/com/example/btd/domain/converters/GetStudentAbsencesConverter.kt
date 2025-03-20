package com.example.btd.domain.converters

import com.example.btd.data.models.AbsenceModel
import com.example.btd.data.models.StudentAbsenceModel
import com.example.btd.domain.models.Result
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.AddAllAbsenceUseCase
import com.example.btd.domain.use_cases.GetStudentAbsencesUseCase

class GetStudentAbsencesConverter {
    fun convert(registerResult: Result<GetStudentAbsencesUseCase.Response>): UiState<List<StudentAbsenceModel>> =
        when (registerResult) {
            is Result.Error -> {
                UiState.Error(
                    registerResult.exception
                )
            }

            is Result.Success -> {
                UiState.Success(
                    registerResult.data.absences
                )
            }
        }
}