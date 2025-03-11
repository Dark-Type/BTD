package com.example.btd.data.remote.data_source.implementation

import com.example.btd.data.models.CreateAbsenceModel
import com.example.btd.data.models.EditAbsenceModel
import com.example.btd.data.networking.AuthInterceptor
import com.example.btd.data.networking.NetworkModule
import com.example.btd.data.remote.api_services.ApiServiceAbsent
import com.example.btd.data.remote.data_source.interfaces.AbsenceRemoteDataSource
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AbsenceRemoteDataSourceImpl(
    private val apiAbsenceService: ApiServiceAbsent = NetworkModule().provideService<ApiServiceAbsent>(
        NetworkModule().provideRetrofit(
            NetworkModule().provideOkHttpClient(
                AuthInterceptor()
            )

        )
    ),
) : AbsenceRemoteDataSource {

    override fun createAbsence(createAbsenceDto: CreateAbsenceModel) = flow {
        emit(apiAbsenceService.createAbsence(createAbsenceDto))
    }

    override fun addFileToAbsence(
        id: String,
        name: RequestBody,
        description: RequestBody,
        file: MultipartBody.Part,
    ) = flow {
        emit(apiAbsenceService.addFileToAbsence(id, name, description, file))
    }

    override fun deleteFileFromAbsence(id: String) = flow {
        emit(apiAbsenceService.deleteFileFromAbsence(id))
    }

    override fun deleteAbsence(id: String) = flow {
        emit(apiAbsenceService.deleteAbsence(id))
    }

    override fun editAbsence(id: String, editAbsenceModel: EditAbsenceModel) = flow {
        emit(apiAbsenceService.editAbsence(id, editAbsenceModel))
    }

}