package com.example.btd.domain.use_cases

import com.example.btd.data.models.EditProfileModel
import com.example.btd.data.repository.StudentRepositoryImpl
import com.example.btd.domain.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EditProfileUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val studentRepository: StudentRepository = StudentRepositoryImpl(),
) : UseCase<EditProfileUseCase.Request, EditProfileUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> =
        studentRepository.editStudentProfile(request.editProfileModel).map { Response(it) }

    data class Request(val editProfileModel: EditProfileModel) : UseCase.Request

    data class Response(val editProfileModel: EditProfileModel) : UseCase.Response
}