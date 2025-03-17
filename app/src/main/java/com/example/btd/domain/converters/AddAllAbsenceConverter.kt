package com.example.btd.domain.converters

import com.example.btd.data.models.AbsenceModel
import com.example.btd.domain.models.Result
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.AddAllAbsenceUseCase

class AddAllAbsenceConverter {
    fun convert(registerResult: Result<AddAllAbsenceUseCase.Response>): UiState<List<AbsenceModel>> =
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