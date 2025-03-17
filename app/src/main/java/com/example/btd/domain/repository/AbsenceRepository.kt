package com.example.btd.domain.repository

import com.example.btd.data.models.AbsenceModel
import com.example.btd.data.models.ConfirmationFileModel
import com.example.btd.data.models.CreateAbsenceModel
import com.example.btd.data.models.EditAbsenceModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AbsenceRepository {
    fun createAbsence(createAbsenceDto: CreateAbsenceModel): Flow<AbsenceModel>
    fun addFileToAbsence(
        id: String,
        name: String,
        description: String,
        file: MultipartBody.Part,
    ): Flow<ConfirmationFileModel>

    fun deleteFileFromAbsence(id: String): Flow<Unit>

    fun deleteAbsence(id: String): Flow<Unit>


    fun editAbsence(id: String, editAbsenceModel: EditAbsenceModel): Flow<AbsenceModel>
}