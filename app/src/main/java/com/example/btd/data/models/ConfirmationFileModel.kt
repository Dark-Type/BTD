package com.example.btd.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmationFileModel(
    val id: String,
    val name: String,
    val description: String,
    val file: String?
)