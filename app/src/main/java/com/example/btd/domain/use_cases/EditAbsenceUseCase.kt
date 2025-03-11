package com.example.btd.domain.use_cases

import com.example.btd.data.models.EditAbsenceModel
import com.example.btd.data.repository.AbsenceRepositoryImpl
import com.example.btd.domain.repository.AbsenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EditAbsenceUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val absenceRepository: AbsenceRepository = AbsenceRepositoryImpl(),
) : UseCase<EditAbsenceUseCase.Request, EditAbsenceUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> =
        absenceRepository.editAbsence(request.id, request.editAbsenceModel).map { Response() }

    data class Request(val editAbsenceModel: EditAbsenceModel, val id: String) : UseCase.Request

    class Response : UseCase.Response
}