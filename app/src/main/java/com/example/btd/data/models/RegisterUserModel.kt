package com.example.btd.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserModel(
    val surname: String,
    val name: String,
    val patronymic: String,
    val email: String,
    val password: String,
    val phoneNumber: String?,
)
