package com.example.btd.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btd.DIContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authService = DIContainer.authService

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
                _loginState.value = LoginResult.Error("Enter a valid email and password cannot be empty.")
                return@launch
            }
            val success = authService.login(email, password, role)
            _loginState.value = if (success) LoginResult.Success else LoginResult.Error("Invalid credentials.")
        }
    }

    fun registerTeacher(
        name: String,
        surname: String,
        email: String,
        faculty: String,
        password: String,
        verifyPassword: String
    ) {
        viewModelScope.launch {
            if (name.isBlank() || surname.isBlank() || !isValidEmail(email) ||
                faculty.isBlank() || password.isBlank() || verifyPassword.isBlank()
            ) {
                _registerState.value = RegisterResult.Error("Please fill out all required fields with valid values.")
                return@launch
            }
            if (password != verifyPassword) {
                _registerState.value = RegisterResult.Error("Passwords do not match.")
                return@launch
            }
            val success = authService.registerTeacher(name, surname, email, faculty, password)
            _registerState.value = if (success) RegisterResult.Success else RegisterResult.Error("Registration failed.")
        }
    }

    fun registerStudent(
        name: String,
        surname: String,
        email: String,
        faculty: String,
        group: String,
        password: String,
        verifyPassword: String
    ) {
        viewModelScope.launch {
            if (name.isBlank() || surname.isBlank() || email.isBlank() ||
                faculty.isBlank() || group.isBlank() || password.isBlank() || verifyPassword.isBlank()
            ) {
                _registerState.value = RegisterResult.Error("Please fill out all fields with valid values.")
                return@launch
            }
            if (!isValidEmail(email)){
                _registerState.value = RegisterResult.Error("Please verify your email, currently it is not valid.")
                return@launch
            }
            if (password != verifyPassword) {
                _registerState.value = RegisterResult.Error("Passwords do not match.")
                return@launch
            }
            val success = authService.registerStudent(name, surname, email, faculty, group, password)
            _registerState.value = if (success) RegisterResult.Success else RegisterResult.Error("Registration failed.")
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}