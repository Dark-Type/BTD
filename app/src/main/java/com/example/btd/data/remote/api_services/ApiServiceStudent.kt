package com.example.btd.data.remote.api_services

import com.example.btd.data.models.EditProfileModel
import com.example.btd.data.models.LoginModel
import com.example.btd.data.models.RegisterStudentModel
import com.example.btd.data.models.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiServiceStudent {
    @POST("/api/student/register")
    suspend fun postRegister(@Body registerStudentModel: RegisterStudentModel): TokenResponse

    @POST("/api/student/login")
    suspend fun postLogin(@Body loginCredentials: LoginModel): TokenResponse

    @GET("/api/student/profile")
    suspend fun getProfile(): EditProfileModel


    @PUT("/api/student/profile")
    suspend fun editStudentProfile(@Body editProfileModel: EditProfileModel): EditProfileModel

}