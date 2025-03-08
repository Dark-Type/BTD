package com.example.btd.domain.use_cases

import com.example.btd.data.models.EditProfileModel
import com.example.btd.data.repository.StudentRepositoryImpl
import com.example.btd.domain.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProfileUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val studentRepository: StudentRepository = StudentRepositoryImpl(),
) : UseCase<GetProfileUseCase.Request, GetProfileUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> =
        studentRepository.getProfile().map { Response(it) }

    class Request : UseCase.Request

    data class Response(val profileModel: EditProfileModel) : UseCase.Response
}