package com.example.btd.domain.use_cases

import com.example.btd.data.models.AbsenceModel
import com.example.btd.data.models.AbsenceStatus
import com.example.btd.data.remote.data_source.implementation.AbsenceRemoteDataSourceImpl
import com.example.btd.data.remote.data_source.interfaces.AbsenceRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AddAllAbsenceUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val absenceRepository: AbsenceRemoteDataSource = AbsenceRemoteDataSourceImpl(),
) : UseCase<AddAllAbsenceUseCase.Request, AddAllAbsenceUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> = absenceRepository.getAllAbsence(
        request.statuses,
        request.ascBoolean,
        request.year,
        request.month,
    ).map { Response(it) }

    data class Request(
        val statuses: AbsenceStatus?,
        val ascBoolean: Boolean?,
        val year: Int,
        val month: Int,
    ) : UseCase.Request

    data class Response(val absences: List<AbsenceModel>) : UseCase.Response
}