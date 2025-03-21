package com.example.btd.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateAbsenceModel(
    val from: String,
    val to: String,
    val reason: String,
)
