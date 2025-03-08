package com.example.btd.domain.converters

import com.example.btd.data.models.EditProfileModel
import com.example.btd.domain.models.Result
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.EditProfileUseCase
import com.example.btd.domain.use_cases.GetProfileUseCase

class EditProfileConverter {
    fun convert(editResult: Result<EditProfileUseCase.Response>): UiState<EditProfileModel> =
        when (editResult) {
            is Result.Error -> {
                UiState.Error(
                    editResult.exception
                )
            }

            is Result.Success -> {
                UiState.Success(editResult.data.editProfileModel)
            }
        }
}