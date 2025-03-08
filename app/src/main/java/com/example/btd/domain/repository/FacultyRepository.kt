package com.example.btd.domain.repository

import com.example.btd.data.models.FacultyModel
import com.example.btd.data.models.GroupModel
import kotlinx.coroutines.flow.Flow

interface FacultyRepository {
    fun getFaculty(): Flow<List<FacultyModel>>


    fun getGroups(id: String): Flow<List<GroupModel>>


}