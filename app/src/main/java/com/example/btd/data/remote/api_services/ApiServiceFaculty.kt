package com.example.btd.data.remote.api_services

import com.example.btd.data.models.FacultyModel
import com.example.btd.data.models.GroupModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServiceFaculty {
    @GET("/api/faculty")
    suspend fun getFaculty(): List<FacultyModel>


    @GET("/api/faculty/{id}/group")
    suspend fun getGroups(@Path("id") id: String): List<GroupModel>
}