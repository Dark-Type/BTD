package com.example.btd.data.models

import kotlinx.serialization.Serializable

@Serializable
data class EditAbsenceModel(
    val to: String,
    val reason: String,
)