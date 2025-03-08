package com.example.btd.data.repository

import com.example.btd.data.models.FacultyModel
import com.example.btd.data.models.GroupModel
import com.example.btd.data.remote.data_source.implementation.FacultyRemoteDataSourceImpl
import com.example.btd.domain.repository.FacultyRepository
import kotlinx.coroutines.flow.Flow

class FacultyRepositoryImpl(
    private val facultyRemoteDataSource: FacultyRemoteDataSourceImpl =
        FacultyRemoteDataSourceImpl(),
) : FacultyRepository {
    override fun getFaculty(): Flow<List<FacultyModel>> =
        facultyRemoteDataSource.getFaculty()

    override fun getGroups(id: String): Flow<List<GroupModel>> =
        facultyRemoteDataSource.getGroups(id)

}