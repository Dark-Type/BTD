package com.example.btd.data.models

import kotlinx.serialization.Serializable

@Serializable
data class GroupModel(
    val id: String,
    val number: String,
    private val facultyId: String,
)