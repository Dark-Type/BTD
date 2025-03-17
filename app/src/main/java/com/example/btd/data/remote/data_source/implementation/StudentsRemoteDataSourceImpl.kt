package com.example.btd.data.remote.data_source.implementation

import com.example.btd.data.models.EditProfileModel
import com.example.btd.data.models.LoginModel
import com.example.btd.data.models.RegisterStudentModel
import com.example.btd.data.models.RegisterUserModel
import com.example.btd.data.models.TokenResponse
import com.example.btd.data.networking.AuthInterceptor
import com.example.btd.data.networking.NetworkModule
import com.example.btd.data.remote.api_services.ApiServiceStudent
import com.example.btd.data.remote.data_source.interfaces.StudentsRemoteDataSource
import com.example.btd.domain.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class StudentsRemoteDataSourceImpl(
    private val apiStudentServiceAuth: ApiServiceStudent = NetworkModule().provideService<ApiServiceStudent>(
        NetworkModule().provideRetrofit(
            NetworkModule().provideOkHttpClient(
                AuthInterceptor(TokenManager.getInstance().getToken())
            )
        )
    ),
    private val apiStudentService: ApiServiceStudent = NetworkModule().provideService<ApiServiceStudent>(
        NetworkModule().provideRetrofit(
            NetworkModule().provideOkHttpClient()
        )
    ),
) : StudentsRemoteDataSource {
    override fun login(loginModel: LoginModel): Flow<TokenResponse> = flow {
        emit(apiStudentService.postLogin(loginModel))
    }.map {
        TokenResponse(it.token)
    }

    override fun registration(registerStudentModel: RegisterStudentModel): Flow<TokenResponse> =
        flow {
            emit(apiStudentService.postRegister(registerStudentModel))
        }.map {
            TokenResponse(it.token)
        }

    override fun registrationUser(registerStudentModel: RegisterUserModel): Flow<TokenResponse> =
        flow {
            emit(apiStudentService.postUserRegister(registerStudentModel))
        }.map {
            TokenResponse(it.token)
        }


    override fun getProfile(): Flow<EditProfileModel> = flow {
        emit(apiStudentServiceAuth.getProfile())
    }.map {
        EditProfileModel(
            it.email, it.surname, it.name, it.patronymic, it.phoneNumber, it.groups
        )
    }

    override fun editStudentProfile(editProfileModel: EditProfileModel): Flow<EditProfileModel> =
        flow {
            emit(apiStudentServiceAuth.editStudentProfile(editProfileModel))
        }.map {
            EditProfileModel(
                it.email, it.surname, it.name, it.patronymic, it.phoneNumber, it.groups
            )
        }
}