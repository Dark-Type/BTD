package com.example.btd.services

import android.util.Log
import com.example.btd.data.models.CreateAbsenceModel
import com.example.btd.domain.converters.AddFileToAbsenceConverter
import com.example.btd.domain.converters.CreateAbsenceConverter
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.AddFileToAbsenceUseCase
import com.example.btd.domain.use_cases.CreateAbsenceUseCase
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import java.time.LocalDate

data class AbsenceSubmission(
    val id: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val document: String?,
    val status: SubmissionStatus,
)

enum class SubmissionStatus {
    Accepted, Declined, Pending
}

interface StudentAbsenceService {

    suspend fun submitAbsence(
        startDate: String,
        endDate: String,
        reason: String, name: String?,
        document: List<MultipartBody.Part>,
    )

    suspend fun refreshStatuses(submissions: List<AbsenceSubmission>): List<AbsenceSubmission>
}

object MockStudentAbsenceService : StudentAbsenceService {
    private val submissions = mutableListOf<AbsenceSubmission>()
    private var idCounter = 1

    private val createAbsenceUseCase = CreateAbsenceUseCase()
    private val addFileToAbsenceUseCase = AddFileToAbsenceUseCase()

    private val createAbsenceConverter = CreateAbsenceConverter()
    private val addFileToAbsenceConverter = AddFileToAbsenceConverter()


    override suspend fun submitAbsence(
        startDate: String,
        endDate: String,
        reason: String,
        name: String?,
        document: List<MultipartBody.Part>,
    ) {
        val submission = CreateAbsenceModel(
            from = startDate,
            to = endDate,
            reason = reason,
        )
        createAbsenceUseCase.execute(CreateAbsenceUseCase.Request(submission)).map {
            createAbsenceConverter.convert(it)
        }.collect {
            when (it) {
                is UiState.Success -> {
                    val absence = it
                    for (i in document.indices) {
                        addFileToAbsenceUseCase.execute(
                            AddFileToAbsenceUseCase.Request(
                                absence.data.id, name ?: "name", "", document[i]
                            )
                        ).map {
                            addFileToAbsenceConverter.convert(it)
                        }.collect{

                            if(it is UiState.Error)
                                Log.d("ssvm","error2: ${it.errorMessage}")

                        }
                    }
                }

                else -> {
                    if(it is UiState.Error)
                        Log.d("ssvm","error1: ${it.errorMessage}")

                }
            }
        }

    }

    override suspend fun refreshStatuses(submissions: List<AbsenceSubmission>): List<AbsenceSubmission> {
        val randomStatuses =
            listOf(SubmissionStatus.Accepted, SubmissionStatus.Declined, SubmissionStatus.Pending)
        val updatedSubmissions = submissions.map { it.copy(status = randomStatuses.random()) }
        this.submissions.clear()
        this.submissions.addAll(updatedSubmissions)
        return updatedSubmissions
    }
}