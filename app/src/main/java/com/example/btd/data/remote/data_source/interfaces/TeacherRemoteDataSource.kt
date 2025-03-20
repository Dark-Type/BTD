package com.example.btd.data.remote.data_source.interfaces

import com.example.btd.data.models.AbsenceModel

import com.example.btd.data.models.StudentAbsenceModel
import kotlinx.coroutines.flow.Flow

interface TeacherRemoteDataSource {
    fun getStudentAbsences(studentId: String,
                           year: Int,
                           month: Int,): Flow<List<AbsenceModel>>
    fun getStudent(
        year: Int,
        month: Int,
    ): Flow<List<StudentAbsenceModel>>


}