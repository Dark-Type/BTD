package com.example.btd.data.models

import kotlinx.serialization.Serializable

@Serializable
data class StudentAbsenceModel(
    val studentId: String,
    val name: String,
    val surname: String,
    val patronymic: String,
    val faculties: List<FacultyModel>,
    val groups: List<GroupModel>,
    val absences: List<AbsenceModel>,
)
