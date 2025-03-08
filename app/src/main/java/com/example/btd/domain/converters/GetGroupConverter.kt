package com.example.btd.domain.converters

import com.example.btd.data.models.GroupModel
import com.example.btd.domain.models.Result
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.GetGroupsUseCase

class GetGroupConverter {
    fun convert(getResult: Result<GetGroupsUseCase.Response>): UiState<List<GroupModel>> =
        when (getResult) {
            is Result.Error -> {
                UiState.Error(
                    getResult.exception
                )
            }

            is Result.Success -> {
                UiState.Success(getResult.data.groupList)
            }
        }
}