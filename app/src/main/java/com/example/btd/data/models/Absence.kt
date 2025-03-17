package com.example.btd.data.models

import kotlinx.serialization.Serializable


data class Absence(
    val id: String,
    val from: String,
    val to: String,
    val reason: String,
    val status: AbsenceStatus,
    val studentId: String,
    val files: List<ConfirmationFile> = emptyList(),
    val student: Any,
)