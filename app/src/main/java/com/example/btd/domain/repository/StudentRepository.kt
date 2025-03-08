package com.example.btd.domain.repository

import com.example.btd.data.models.EditProfileModel
import com.example.btd.data.models.LoginModel
import com.example.btd.data.models.RegisterStudentModel
import com.example.btd.data.models.TokenResponse
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    fun login(loginModel: LoginModel): Flow<TokenResponse>

    fun registration(registerStudentModel: RegisterStudentModel): Flow<TokenResponse>

    fun getProfile(): Flow<EditProfileModel>

    fun editStudentProfile(editProfileModel: EditProfileModel): Flow<EditProfileModel>

}