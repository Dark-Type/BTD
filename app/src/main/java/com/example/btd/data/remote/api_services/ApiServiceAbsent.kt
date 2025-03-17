package com.example.btd.data.remote.api_services

import com.example.btd.data.models.AbsenceModel
import com.example.btd.data.models.AbsenceStatus
import com.example.btd.data.models.ConfirmationFileModel
import com.example.btd.data.models.CreateAbsenceModel
import com.example.btd.data.models.EditAbsenceModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceAbsent {

    @POST("/api/absence")
    suspend fun createAbsence(@Body createAbsenceDto: CreateAbsenceModel): AbsenceModel

    @GET("/api/absence")
    suspend fun getAllAbsence(
        @Query("Statuses") statuses: String?,
        @Query("AscSorting") ascSorting: Boolean?,
        @Query("Year") year: Int,
        @Query("Month") month: Int,
    ): List<AbsenceModel>

    @Multipart
    @POST("/api/absence/{id}/file")
    suspend fun addFileToAbsence(
        @Path("id") id: String,
        @Part("Name") name: String,
        @Part("Description") description: String,
        @Part file: MultipartBody.Part,
    ): ConfirmationFileModel

    @DELETE("/api/absence/file/{id}")
    suspend fun deleteFileFromAbsence(@Path("id") id: String)

    @DELETE("/api/absence/{id}")
    suspend fun deleteAbsence(@Path("id") id: String)

    @PUT("/api/absence/{id}")
    suspend fun editAbsence(
        @Path("id") id: String,
        @Body editAbsenceModel: EditAbsenceModel,
    ): AbsenceModel
}