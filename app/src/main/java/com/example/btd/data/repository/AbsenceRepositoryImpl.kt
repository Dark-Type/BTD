package com.example.btd.data.repository

import com.example.btd.data.models.AbsenceModel
import com.example.btd.data.models.ConfirmationFileModel
import com.example.btd.data.models.CreateAbsenceModel
import com.example.btd.data.models.EditAbsenceModel
import com.example.btd.data.remote.data_source.implementation.AbsenceRemoteDataSourceImpl
import com.example.btd.data.remote.data_source.interfaces.AbsenceRemoteDataSource
import com.example.btd.domain.repository.AbsenceRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AbsenceRepositoryImpl(
    private val absenceRemoteDataSource: AbsenceRemoteDataSource = AbsenceRemoteDataSourceImpl(),
) : AbsenceRepository {
    override fun createAbsence(createAbsenceDto: CreateAbsenceModel): Flow<AbsenceModel> =
        absenceRemoteDataSource.createAbsence(createAbsenceDto)

    override fun addFileToAbsence(
        id: String,
        name: String,
        description: String,
        file: MultipartBody.Part,
    ): Flow<ConfirmationFileModel> = absenceRemoteDataSource.addFileToAbsence(id, name, description, file)

    override fun deleteFileFromAbsence(id: String): Flow<Unit> =
        absenceRemoteDataSource.deleteFileFromAbsence(id)

    override fun deleteAbsence(id: String): Flow<Unit> = absenceRemoteDataSource.deleteAbsence(id)

    override fun editAbsence(id: String, editAbsenceModel: EditAbsenceModel): Flow<AbsenceModel> =
        absenceRemoteDataSource.editAbsence(id, editAbsenceModel)

}