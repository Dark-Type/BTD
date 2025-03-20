package com.example.btd.data.remote.api_services

import com.example.btd.data.models.AbsenceModel
import com.example.btd.data.models.StudentAbsenceModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceTeacher {
    @GET("/api/teacher/absences")
    suspend fun getStudentAbsences(
        @Query("studentId") studentId: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): List<StudentAbsenceModel>

    @GET("/api/teacher/list")
    suspend fun getStudent(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): List<StudentAbsenceModel>
}