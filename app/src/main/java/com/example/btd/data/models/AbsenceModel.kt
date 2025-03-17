package com.example.btd.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AbsenceModel(
    val id: String,
    val from: String,
    val to: String,
    val reason: String,
    val status: AbsenceStatus,
    val files: List<ConfirmationFileModel> = ArrayList(),
)