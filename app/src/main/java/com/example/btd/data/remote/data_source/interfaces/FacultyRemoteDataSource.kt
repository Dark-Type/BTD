package com.example.btd.data.remote.data_source.interfaces

import com.example.btd.data.models.FacultyModel
import com.example.btd.data.models.GroupModel
import kotlinx.coroutines.flow.Flow

interface FacultyRemoteDataSource {
    fun getFaculty(): Flow<List<FacultyModel>>


    fun getGroups(id: String): Flow<List<GroupModel>>
}