package com.example.btd.domain.use_cases

import com.example.btd.data.models.GroupModel
import com.example.btd.data.models.LoginModel
import com.example.btd.data.models.RegisterStudentModel
import com.example.btd.data.models.TokenResponse
import com.example.btd.data.repository.FacultyRepositoryImpl
import com.example.btd.data.repository.StudentRepositoryImpl
import com.example.btd.domain.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetGroupsUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val facultyRepository: FacultyRepositoryImpl = FacultyRepositoryImpl(),
) : UseCase<GetGroupsUseCase.Request, GetGroupsUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> =
        facultyRepository.getGroups(request.id).map { Response(it) }

    data class Request(val id: String) : UseCase.Request

    data class Response(val groupList: List<GroupModel>) : UseCase.Response
}