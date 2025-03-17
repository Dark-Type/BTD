package com.example.btd.data.remote.data_source.interfaces

import com.example.btd.data.models.AbsenceModel
import com.example.btd.data.models.AbsenceStatus
import com.example.btd.data.models.ConfirmationFileModel
import com.example.btd.data.models.CreateAbsenceModel
import com.example.btd.data.models.EditAbsenceModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Query

interface AbsenceRemoteDataSource {
    fun createAbsence(createAbsenceDto: CreateAbsenceModel): Flow<AbsenceModel>
    fun addFileToAbsence(
        id: String,
        name: String,
        description: String,
        file: MultipartBody.Part,
    ): Flow<ConfirmationFileModel>

    fun deleteFileFromAbsence(id: String): Flow<Unit>

    fun deleteAbsence(id: String): Flow<Unit>

    fun getAllAbsence(
        @Query("Statuses") statuses: AbsenceStatus?,
        @Query("AscSorting") ascSorting: Boolean?,
        @Query("Year") year: Int,
        @Query("Month") month: Int,
    ): Flow<List<AbsenceModel>>


    fun editAbsence(id: String, editAbsenceModel: EditAbsenceModel): Flow<AbsenceModel>

}