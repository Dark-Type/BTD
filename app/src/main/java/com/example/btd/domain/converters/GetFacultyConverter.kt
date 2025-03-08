package com.example.btd.domain.converters

import com.example.btd.data.models.EditProfileModel
import com.example.btd.data.models.FacultyModel
import com.example.btd.domain.models.Result
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.GetFacultyUseCase
import com.example.btd.domain.use_cases.GetProfileUseCase

class GetFacultyConverter {
    fun convert(getResult: Result<GetFacultyUseCase.Response>): UiState<List<FacultyModel>> =
        when (getResult) {
            is Result.Error -> {
                UiState.Error(
                    getResult.exception
                )
            }

            is Result.Success -> {
                UiState.Success(getResult.data.facultyList)
            }
        }
}