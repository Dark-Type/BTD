package com.example.btd

import com.example.btd.interfaces.AuthService
import com.example.btd.services.TestAuthService

object DIContainer {
    val authService: AuthService by lazy { TestAuthService() }
}