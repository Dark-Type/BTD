package com.example.btd.domain.converters

import com.example.btd.data.models.AbsenceModel
import com.example.btd.domain.models.Result
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.CreateAbsenceUseCase

class CreateAbsenceConverter {
    fun convert(result: Result<CreateAbsenceUseCase.Response>): UiState<AbsenceModel> =
        when (result) {
            is Result.Error -> {
                UiState.Error(
                    result.exception
                )
            }

            is Result.Success -> {
                UiState.Success(result.data.absenceModel)
            }
        }
}