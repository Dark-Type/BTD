package com.example.btd.domain.converters

import com.example.btd.data.models.TokenResponse
import com.example.btd.domain.models.Result
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.LoginUseCase

class LoginConverter {
    fun convert(registerResult: Result<LoginUseCase.Response>): UiState<TokenResponse> =
        when (registerResult) {
            is Result.Error -> {
                UiState.Error(
                    registerResult.exception
                )
            }

            is Result.Success -> {
                UiState.Success(TokenResponse(registerResult.data.authToken.token))
            }
        }
}