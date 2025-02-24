package com.example.btd.services

import java.time.LocalDate

data class AbsenceSubmission(
    val id: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val document: String?,
    val status: SubmissionStatus
)

enum class SubmissionStatus {
    Accepted, Declined, Pending
}

interface StudentAbsenceService {
    suspend fun getSubmissions(): List<AbsenceSubmission>
    suspend fun submitAbsence(startDate: LocalDate, endDate: LocalDate, document: String?): AbsenceSubmission
    suspend fun refreshStatuses(submissions: List<AbsenceSubmission>): List<AbsenceSubmission>
}

object MockStudentAbsenceService : StudentAbsenceService {
    private val submissions = mutableListOf<AbsenceSubmission>()
    private var idCounter = 1

    override suspend fun getSubmissions(): List<AbsenceSubmission> {
        return submissions.toList()
    }

    override suspend fun submitAbsence(startDate: LocalDate, endDate: LocalDate, document: String?): AbsenceSubmission {
        val submission = AbsenceSubmission(
            id = idCounter++,
            startDate = startDate,
            endDate = endDate,
            document = document,
            status = SubmissionStatus.Pending
        )
        submissions.add(submission)
        return submission
    }

    override suspend fun refreshStatuses(submissions: List<AbsenceSubmission>): List<AbsenceSubmission> {
        val randomStatuses = listOf(SubmissionStatus.Accepted, SubmissionStatus.Declined, SubmissionStatus.Pending)
        val updatedSubmissions = submissions.map { it.copy(status = randomStatuses.random()) }
        this.submissions.clear()
        this.submissions.addAll(updatedSubmissions)
        return updatedSubmissions
    }
}