package com.example.btd.data.remote.data_source.implementation

import com.example.btd.data.models.AbsenceModel
import com.example.btd.data.models.StudentAbsenceModel
import com.example.btd.data.networking.AuthInterceptor
import com.example.btd.data.networking.NetworkModule
import com.example.btd.data.remote.api_services.ApiServiceTeacher
import com.example.btd.data.remote.data_source.interfaces.TeacherRemoteDataSource
import com.example.btd.domain.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TeacherRemoteDataSourceImpl(
    private val apiAbsenceService: ApiServiceTeacher = NetworkModule().provideService<ApiServiceTeacher>(
        NetworkModule().provideRetrofit(
            NetworkModule().provideOkHttpClient(
                AuthInterceptor(TokenManager.getInstance().getToken())
            )

        )
    ),
) : TeacherRemoteDataSource {

    override fun getStudentAbsences(
        studentId: String,
        year: Int,
        month: Int,
    ): Flow<List<AbsenceModel>> {
        try {
            return flow {
                emit(apiAbsenceService.getStudentAbsences(studentId, year, month))
            }
        } catch (
            e: Exception
        ) {
            throw e
        }

    }


    override fun getStudent(
        year: Int,
        month: Int,
    ): Flow<List<StudentAbsenceModel>> {
        try {
            return flow {
                emit(apiAbsenceService.getStudent(year, month))
            }
        } catch (e: Exception) {
            throw e
        }
    }

}