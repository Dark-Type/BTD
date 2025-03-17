package com.example.btd.domain

import android.content.Context
import android.content.SharedPreferences

class TokenManager private constructor() {

    private var token: String? = null

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        @Volatile
        private var instance: TokenManager? = null

        fun getInstance(context: Context): TokenManager {
            return instance ?: synchronized(this) {
                instance ?: TokenManager().also {
                    it.init(context)
                    instance = it
                }
            }
        }

        fun getInstance(): TokenManager {
            return instance
                ?: throw IllegalStateException("TokenManager must be initialized with context first!")
        }
    }

    private fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("TokenPrefs", Context.MODE_PRIVATE)
    }

    fun saveToken(newToken: String) {
        token = newToken
        sharedPreferences.edit().putString("token", newToken).apply()
    }

    fun getToken(): String {
        if (token == null) {
            token = sharedPreferences.getString("token", null) ?: ""
        }
        return token ?: ""
    }

    fun clearToken() {
        token = null
        sharedPreferences.edit().remove("token").apply()
    }

    fun hasToken(): Boolean {
        return getToken() != null
    }
}