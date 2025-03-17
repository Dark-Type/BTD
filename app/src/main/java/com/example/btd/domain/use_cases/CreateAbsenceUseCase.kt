package com.example.btd.domain.use_cases

import com.example.btd.data.models.AbsenceModel
import com.example.btd.data.models.CreateAbsenceModel
import com.example.btd.data.repository.AbsenceRepositoryImpl
import com.example.btd.domain.repository.AbsenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CreateAbsenceUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val absenceRepository: AbsenceRepository = AbsenceRepositoryImpl(),
) : UseCase<CreateAbsenceUseCase.Request, CreateAbsenceUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> =
        absenceRepository.createAbsence(request.createAbsenceModel).map { Response(it) }

    data class Request(val createAbsenceModel: CreateAbsenceModel) : UseCase.Request

    class Response(val absenceModel: AbsenceModel) : UseCase.Response
}