package com.example.btd.data.networking

data class NetworkConfig(
    val baseUrl: String,
    val readTimeout: Long = 25,
    val connectTimeout: Long = 25,
    val writeTimeout: Long = 25
)
