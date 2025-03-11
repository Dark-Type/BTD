package com.example.btd.domain.repository

import com.example.btd.data.models.CreateAbsenceModel
import com.example.btd.data.models.EditAbsenceModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AbsenceRepository {
    fun createAbsence(createAbsenceDto: CreateAbsenceModel): Flow<Unit>
    fun addFileToAbsence(
        id: String,
        name: RequestBody,
        description: RequestBody,
        file: MultipartBody.Part,
    ): Flow<Unit>

    fun deleteFileFromAbsence(id: String): Flow<Unit>

    fun deleteAbsence(id: String): Flow<Unit>


    fun editAbsence(id: String, editAbsenceModel: EditAbsenceModel): Flow<Unit>
}