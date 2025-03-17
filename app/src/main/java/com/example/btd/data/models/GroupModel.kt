package com.example.btd.data.models

import kotlinx.serialization.Serializable

@Serializable
data class GroupModel(
    private val id: String,
    private val number: String,
    private val facultyId: String,
)