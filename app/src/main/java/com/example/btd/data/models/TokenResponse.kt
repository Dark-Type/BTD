package com.example.btd.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String,
)
