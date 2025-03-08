package com.example.btd.domain.use_cases

import com.example.btd.data.models.LoginModel
import com.example.btd.data.models.RegisterStudentModel
import com.example.btd.data.models.TokenResponse
import com.example.btd.data.repository.StudentRepositoryImpl
import com.example.btd.domain.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val studentRepository: StudentRepository = StudentRepositoryImpl(),
) : UseCase<LoginUseCase.Request, LoginUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> =
        studentRepository.login(request.loginData).map { Response(it) }

    data class Request(val loginData: LoginModel) : UseCase.Request

    data class Response(val authToken: TokenResponse) : UseCase.Response
}