package com.example.btd.domain.converters

import com.example.btd.domain.models.Result
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.CreateAbsenceUseCase

class CreateAbsenceConverter {
    fun convert(result: Result<CreateAbsenceUseCase.Response>): UiState<String> =
        when (result) {
            is Result.Error -> {
                UiState.Error(
                    result.exception
                )
            }

            is Result.Success -> {
                UiState.Success("Success")
            }
        }
}