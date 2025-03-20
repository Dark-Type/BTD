package com.example.btd.data.remote.data_source.interfaces

import com.example.btd.data.models.AbsenceModel
import com.example.btd.data.models.AbsenceStatus
import com.example.btd.data.models.ConfirmationFileModel
import com.example.btd.data.models.CreateAbsenceModel
import com.example.btd.data.models.EditAbsenceModel
import com.example.btd.data.models.StudentAbsenceModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Query

interface TeacherRemoteDataSource {
    fun getStudentAbsences(studentId: String,
                           year: Int,
                           month: Int,): Flow<List<AbsenceModel>>
    fun getStudent(
        year: Int,
        month: Int,
    ): Flow<StudentAbsenceModel>


}