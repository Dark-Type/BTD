package com.example.btd.domain.use_cases

import com.example.btd.data.models.RegisterStudentModel
import com.example.btd.data.models.TokenResponse
import com.example.btd.data.repository.StudentRepositoryImpl
import com.example.btd.domain.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RegisterUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val studentRepository: StudentRepository = StudentRepositoryImpl(),
) : UseCase<RegisterUseCase.Request, RegisterUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> =
        studentRepository.registration(request.loginData).map { Response(it) }

    data class Request(val loginData: RegisterStudentModel) : UseCase.Request

    data class Response(val authToken: TokenResponse) : UseCase.Response
}