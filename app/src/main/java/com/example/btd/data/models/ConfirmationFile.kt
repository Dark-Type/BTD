package com.example.btd.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmationFile(
    val id: String,
    val absenceId: String,
    val name: String,
    val description: String,
    val file: String
)