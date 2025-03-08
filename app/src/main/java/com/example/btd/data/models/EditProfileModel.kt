package com.example.btd.data.models

import kotlinx.serialization.Serializable

@Serializable
data class EditProfileModel(
    val email: String,
    val surname: String,
    val name: String,
    val patronymic: String,
    val phoneNumber: String?,
    val groups: List<String>
)
