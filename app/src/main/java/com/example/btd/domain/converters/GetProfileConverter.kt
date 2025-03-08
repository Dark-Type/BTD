package com.example.btd.domain.converters

import com.example.btd.data.models.EditProfileModel
import com.example.btd.domain.models.Result
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.GetProfileUseCase

class GetProfileConverter {
    fun convert(getResult: Result<GetProfileUseCase.Response>): UiState<EditProfileModel> =
        when (getResult) {
            is Result.Error -> {
                UiState.Error(
                    getResult.exception
                )
            }

            is Result.Success -> {
                UiState.Success(getResult.data.profileModel)
            }
        }
}