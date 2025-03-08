package com.example.btd.data.networking

data class NetworkConfig(
    val baseUrl: String,
    val readTimeout: Long = 15,
    val connectTimeout: Long = 15,
    val writeTimeout: Long = 15
)
