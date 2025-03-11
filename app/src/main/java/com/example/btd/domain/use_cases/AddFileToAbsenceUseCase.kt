package com.example.btd.domain.use_cases

import com.example.btd.data.repository.AbsenceRepositoryImpl
import com.example.btd.domain.repository.AbsenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddFileToAbsenceUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val absenceRepository: AbsenceRepository = AbsenceRepositoryImpl(),
) : UseCase<AddFileToAbsenceUseCase.Request, AddFileToAbsenceUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> =
        absenceRepository.addFileToAbsence(
            request.id,
            request.name,
            request.description,
            request.file,
        ).map { Response() }

    data class Request(
        val id: String,
        val name: RequestBody,
        val description: RequestBody,
        val file: MultipartBody.Part,
    ) : UseCase.Request

    class Response : UseCase.Response
}