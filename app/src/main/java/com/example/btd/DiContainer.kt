package com.example.btd

import com.example.btd.interfaces.AuthService
import com.example.btd.services.MockAuthService

object DIContainer {
    val authService: AuthService by lazy { MockAuthService() }
}