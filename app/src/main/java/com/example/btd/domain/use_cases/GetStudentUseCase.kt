package com.example.btd.domain.use_cases

import com.example.btd.data.models.StudentAbsenceModel
import com.example.btd.data.remote.data_source.implementation.TeacherRemoteDataSourceImpl
import com.example.btd.data.remote.data_source.interfaces.TeacherRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetStudentUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val absenceRepository: TeacherRemoteDataSource = TeacherRemoteDataSourceImpl(),
) : UseCase<GetStudentUseCase.Request, GetStudentUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> = absenceRepository.getStudent(
        request.year,
        request.month,
    ).map { Response(it) }

    data class Request(
        val year: Int,
        val month: Int,
    ) : UseCase.Request

    data class Response(val absences: List<StudentAbsenceModel> ) : UseCase.Response
}