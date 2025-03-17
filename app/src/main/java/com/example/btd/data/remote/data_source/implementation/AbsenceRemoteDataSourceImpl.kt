package com.example.btd.data.remote.data_source.implementation

import android.util.Log
import com.example.btd.data.models.AbsenceModel
import com.example.btd.data.models.AbsenceStatus
import com.example.btd.data.models.CreateAbsenceModel
import com.example.btd.data.models.EditAbsenceModel
import com.example.btd.data.networking.AuthInterceptor
import com.example.btd.data.networking.NetworkModule
import com.example.btd.data.remote.api_services.ApiServiceAbsent
import com.example.btd.data.remote.data_source.interfaces.AbsenceRemoteDataSource
import com.example.btd.domain.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody

class AbsenceRemoteDataSourceImpl(
    private val apiAbsenceService: ApiServiceAbsent = NetworkModule().provideService<ApiServiceAbsent>(
        NetworkModule().provideRetrofit(
            NetworkModule().provideOkHttpClient(
                AuthInterceptor(TokenManager.getInstance().getToken())
            )

        )
    ),
) : AbsenceRemoteDataSource {

    override fun createAbsence(createAbsenceDto: CreateAbsenceModel): Flow<AbsenceModel> = flow {
        emit(apiAbsenceService.createAbsence(createAbsenceDto))
    }

    override fun addFileToAbsence(
        id: String,
        name: String,
        description: String,
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

    override fun getAllAbsence(
        statuses: AbsenceStatus?,
        ascSorting: Boolean?,
        year: Int,
        month: Int,
    ) = flow {
        emit(apiAbsenceService.getAllAbsence(statuses?.name, ascSorting, year, month))
    }


    override fun editAbsence(id: String, editAbsenceModel: EditAbsenceModel): Flow<AbsenceModel> =
        flow {
            emit(apiAbsenceService.editAbsence(id, editAbsenceModel))
        }

}