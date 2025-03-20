package com.example.btd.domain.use_cases

import com.example.btd.data.models.AbsenceModel
import com.example.btd.data.remote.data_source.implementation.TeacherRemoteDataSourceImpl
import com.example.btd.data.remote.data_source.interfaces.TeacherRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetStudentAbsencesUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val absenceRepository: TeacherRemoteDataSource = TeacherRemoteDataSourceImpl(),
) : UseCase<GetStudentAbsencesUseCase.Request, GetStudentAbsencesUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> = absenceRepository.getStudentAbsences(
        request.studentId,
        request.year,
        request.month,
    ).map { Response(it) }

    data class Request(
        val studentId: String,
        val year: Int,
        val month: Int,
    ) : UseCase.Request

    data class Response(val absences: List<AbsenceModel>) : UseCase.Response
}