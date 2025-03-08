package com.example.btd.domain.use_cases

import com.example.btd.data.models.FacultyModel
import com.example.btd.data.repository.FacultyRepositoryImpl
import com.example.btd.domain.repository.FacultyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFacultyUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val facultyRepository: FacultyRepository = FacultyRepositoryImpl(),
) : UseCase<GetFacultyUseCase.Request, GetFacultyUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> =
        facultyRepository.getFaculty().map { Response(it) }

    class Request : UseCase.Request

    data class Response(val facultyList: List<FacultyModel>) : UseCase.Response
}