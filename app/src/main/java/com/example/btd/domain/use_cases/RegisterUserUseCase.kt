package com.example.btd.domain.use_cases

import com.example.btd.data.models.RegisterUserModel
import com.example.btd.data.models.TokenResponse
import com.example.btd.data.remote.data_source.implementation.StudentsRemoteDataSourceImpl
import com.example.btd.data.remote.data_source.interfaces.StudentsRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RegisterUserUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val studentsRemoteDataSource: StudentsRemoteDataSource = StudentsRemoteDataSourceImpl(),
) : UseCase<RegisterUserUseCase.Request, RegisterUserUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> =
        studentsRemoteDataSource.registrationUser(request.loginData).map { Response(it) }

    data class Request(val loginData: RegisterUserModel) : UseCase.Request

    data class Response(val authToken: TokenResponse) : UseCase.Response
}