package com.example.btd.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btd.DIContainer
import com.example.btd.data.models.RegisterStudentModel
import com.example.btd.data.models.TokenResponse
import com.example.btd.domain.converters.LoginConverter
import com.example.btd.domain.converters.RegisterConverter
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.LoginUseCase
import com.example.btd.domain.use_cases.RegisterUseCase
import com.example.btd.session.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class AuthViewModel(
    private val registerUseCase: RegisterUseCase = RegisterUseCase(),
    private val registerConverter: RegisterConverter = RegisterConverter(),
    private val loginUseCase: LoginUseCase = LoginUseCase(),
    private val loginConverter: LoginConverter = LoginConverter(),
) : ViewModel() {

    private val authService = DIContainer.authService
    private var _registerAnswer = MutableStateFlow<UiState<TokenResponse>>(UiState.Loading)
    val registerAnswer: StateFlow<UiState<TokenResponse>> = _registerAnswer

    private val _loginState = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginState: StateFlow<LoginResult> get() = _loginState

    sealed class LoginResult {
        object Idle : LoginResult()
        object Success : LoginResult()
        data class Error(val message: String) : LoginResult()
    }

    private val _registerState = MutableStateFlow<RegisterResult>(RegisterResult.Idle)
    val registerState: StateFlow<RegisterResult> get() = _registerState

    sealed class RegisterResult {
        object Idle : RegisterResult()
        object Success : RegisterResult()
        data class Error(val message: String) : RegisterResult()
    }

    fun login(email: String, password: String, role: String) {
        viewModelScope.launch {
            if (!isValidEmail(email) || password.isBlank()) {
                _loginState.value =
                    LoginResult.Error("Enter a valid email and password cannot be empty.")
                return@launch
            }

            val success = authService.login(email, password, role)
            if (success) {
                UserSession.isLoggedIn = true
                UserSession.userRole = role
                _loginState.value = LoginResult.Success
            } else {
                _loginState.value = LoginResult.Error("Invalid credentials.")
            }
        }
    }

    fun registerTeacher(
        name: String,
        surname: String,
        email: String,
        faculty: String,
        password: String,
        verifyPassword: String,

        ) {
        viewModelScope.launch {
            if (name.isBlank() || surname.isBlank() || !isValidEmail(email) ||
                faculty.isBlank() || password.isBlank() || verifyPassword.isBlank()
            ) {
                _registerState.value =
                    RegisterResult.Error("Please fill out all required fields with valid values.")
                return@launch
            }
            if (password != verifyPassword) {
                _registerState.value = RegisterResult.Error("Passwords do not match.")
                return@launch
            }


            val success = authService.registerTeacher(name, surname, email, faculty, password)
            if (success) {
                UserSession.isLoggedIn = true
                UserSession.userRole = "teacher"
                _registerState.value = RegisterResult.Success
            } else {
                _registerState.value = RegisterResult.Error("Registration failed.")
            }
        }
    }

    fun registerStudent(
        name: String = "Никита",
        surname: String = "Скажиутин",
        email: String = "skajutin@email.org",
        faculty: String = "08dd5d8a-c1a2-4d3f-81a0-cfa5b12b8dd5",
        group: List<String> = listOf("08dd5e14-1c11-4fbf-81ac-68983bd9dc13"),
        password: String = "ny_password_123",
        verifyPassword: String = "ny_password_123",
        patronymic: String = "No_Information",
        phoneNumber: String = "+7 (913) 884-61-23",
    ) {
        viewModelScope.launch {
            /*if (name.isBlank() || surname.isBlank() || !isValidEmail(email) ||
                faculty.isBlank() || group.isEmpty() || password.isBlank() || verifyPassword.isBlank()
            ) {
                _registerState.value =
                    RegisterResult.Error("Please fill out all required fields with valid values.")
                return@launch
            }
            if (password != verifyPassword) {
                _registerState.value = RegisterResult.Error("Passwords do not match.")
                return@launch
            }*/
            Log.d("response","launch")

            launch {
                registerUseCase.execute(
                    RegisterUseCase.Request(
                        RegisterStudentModel(
                            surname,
                            name,
                            patronymic,
                            email,
                            password,
                            phoneNumber,
                            group
                        )
                    )
                ).map {
                    registerConverter.convert(it)
                }.collect {
                    _registerAnswer.value = it
                    Log.d("response", "it: $it")
                }
            }
            val success =
                authService.registerStudent(name, surname, email, faculty, group[0], password)
            if (success) {
                UserSession.isLoggedIn = true
                UserSession.userRole = "student"
                _registerState.value = RegisterResult.Success
            } else {
                _registerState.value = RegisterResult.Error("Registration failed.")
            }
        }
    }


    private fun isValidEmail(email: String): Boolean {
        val emailPattern: Pattern = Pattern.compile(
            "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        )
        return emailPattern.matcher(email).matches()
    }
}