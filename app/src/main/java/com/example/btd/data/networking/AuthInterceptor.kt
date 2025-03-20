package com.example.btd.data.networking

import android.util.Log
import com.example.btd.session.UserSession
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("Authorization","Bearer: $token")
        return run {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        }
    }
}