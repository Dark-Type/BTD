package com.example.btd.data.networking

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJnaXZlbl9uYW1lIjoibmVraXQudmVyaEBiay5ydSIsIm5iZiI6MTc0MTQxOTczNiwiZXhwIjoxNzQxNDIzMzM2LCJpYXQiOjE3NDE0MTk3MzYsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6NTEyOCIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6NTEyOCJ9.ddEcUjh9yvu7LbybxZsNHnKlpqtLWslUYgMgcYffRxXCvkyXaKo3w3kIcA8_V6w6heQShhyPiGx-Os-GY-aZtw") :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return run {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        }
    }
}