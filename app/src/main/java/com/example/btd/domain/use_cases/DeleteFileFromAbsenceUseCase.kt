package com.example.btd.domain.use_cases

import com.example.btd.data.repository.AbsenceRepositoryImpl
import com.example.btd.domain.repository.AbsenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DeleteFileFromAbsenceUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val absenceRepository: AbsenceRepository = AbsenceRepositoryImpl(),
) : UseCase<DeleteFileFromAbsenceUseCase.Request, DeleteFileFromAbsenceUseCase.Response>(
    configuration
) {
    override fun process(request: Request): Flow<Response> =
        absenceRepository.deleteFileFromAbsence(request.id).map { Response() }

    data class Request(val id: String) : UseCase.Request

    class Response : UseCase.Response
}