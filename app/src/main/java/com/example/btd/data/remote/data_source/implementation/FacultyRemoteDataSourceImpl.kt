package com.example.btd.data.remote.data_source.implementation

import com.example.btd.data.models.FacultyModel
import com.example.btd.data.models.GroupModel
import com.example.btd.data.networking.NetworkModule
import com.example.btd.data.remote.api_services.ApiServiceFaculty
import com.example.btd.data.remote.data_source.interfaces.FacultyRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FacultyRemoteDataSourceImpl(
    private val apiFacultyService: ApiServiceFaculty = NetworkModule().provideService<ApiServiceFaculty>(
        NetworkModule().provideRetrofit(
            NetworkModule().provideOkHttpClient()
        )
    ),
) : FacultyRemoteDataSource {
    override fun getFaculty(): Flow<List<FacultyModel>> = flow {
        emit(apiFacultyService.getFaculty())
    }.map {
        it
    }

    override fun getGroups(id: String): Flow<List<GroupModel>> = flow {
        emit(apiFacultyService.getGroups(id))
    }.map {
        it
    }

}