package com.example.btd.data.repository

import com.example.btd.data.models.EditProfileModel
import com.example.btd.data.models.LoginModel
import com.example.btd.data.models.RegisterStudentModel
import com.example.btd.data.models.TokenResponse
import com.example.btd.data.remote.data_source.implementation.StudentsRemoteDataSourceImpl
import com.example.btd.data.remote.data_source.interfaces.StudentsRemoteDataSource
import com.example.btd.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow

class StudentRepositoryImpl(
    private val studentRemoteDataSource: StudentsRemoteDataSource =
        StudentsRemoteDataSourceImpl(),
) : StudentRepository {
    override fun login(loginModel: LoginModel): Flow<TokenResponse> =
        studentRemoteDataSource.login(loginModel)

    override fun registration(registerStudentModel: RegisterStudentModel): Flow<TokenResponse> =
        studentRemoteDataSource.registration(registerStudentModel)

    override fun getProfile(): Flow<EditProfileModel> =
        studentRemoteDataSource.getProfile()

    override fun editStudentProfile(editProfileModel: EditProfileModel): Flow<EditProfileModel> =
        studentRemoteDataSource.editStudentProfile(editProfileModel)
}