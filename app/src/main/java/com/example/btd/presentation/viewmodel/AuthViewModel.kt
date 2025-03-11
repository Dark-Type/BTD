package com.example.btd.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btd.data.models.LoginModel
import com.example.btd.data.models.RegisterStudentModel
import com.example.btd.data.models.TokenResponse
import com.example.btd.domain.converters.LoginConverter
import com.example.btd.domain.converters.RegisterConverter
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.LoginUseCase
import com.example.btd.domain.use_cases.RegisterUseCase
import com.example.btd.session.UserSession
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val loginUseCase = LoginUseCase()
    private val registerUseCase = RegisterUseCase()


    private val loginConverter = LoginConverter()
    private val registerConverter = RegisterConverter()

    private val _loginState = MutableLiveData<UiState<TokenResponse>>()
    val loginState: LiveData<UiState<TokenResponse>> = _loginState


    private val _registerState = MutableLiveData<UiState<TokenResponse>>()
    val registerState: LiveData<UiState<TokenResponse>> = _registerState


    fun login(email: String, password: String, role: String = "") {
        if (!isValidEmail(email) || password.isBlank()) {
            _loginState.value = UiState.Error("Enter a valid email and password cannot be empty.")
            return
        }

        viewModelScope.launch {
            _loginState.value = UiState.Loading


            val request = LoginUseCase.Request(LoginModel(email, password))
            loginUseCase.execute(request).collect { result ->
                val uiState = loginConverter.convert(result)
                _loginState.value = uiState


                if (uiState is UiState.Success) {
                    UserSession.isLoggedIn = true
                    UserSession.userRole = role
                }
            }
        }
    }


    fun registerStudent(
        name: String,
        surname: String,
        email: String,
        faculty: String,
        group: List<String>,
        password: String,
        verifyPassword: String,
        patronymic: String = "",
        phoneNumber: String? = null
    ) {
        if (name.isBlank() || surname.isBlank() || !isValidEmail(email) ||
            faculty.isBlank() || group.isEmpty() || password.isBlank() || verifyPassword.isBlank()
        ) {
            _registerState.value = UiState.Error("Please fill out all required fields with valid values.")
            return
        }

        if (password != verifyPassword) {
            _registerState.value = UiState.Error("Passwords do not match.")
            return
        }

        viewModelScope.launch {

            _registerState.value = UiState.Loading


            val request = RegisterUseCase.Request(
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

            registerUseCase.execute(request).collect { result ->
                val uiState = registerConverter.convert(result)
                _registerState.value = uiState
                Log.d("AuthViewModel", "Registration result: $uiState")


                if (uiState is UiState.Success) {
                    UserSession.isLoggedIn = true
                    UserSession.userRole = "student"
                }
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
        patronymic: String = ""
    ) {
        if (name.isBlank() || surname.isBlank() || !isValidEmail(email) ||
            faculty.isBlank() || password.isBlank() || verifyPassword.isBlank()
        ) {
            _registerState.value = UiState.Error("Please fill out all required fields with valid values.")
            return
        }

        if (password != verifyPassword) {
            _registerState.value = UiState.Error("Passwords do not match.")
            return
        }


        _registerState.value = UiState.Success(TokenResponse("teacher-mock-token"))
        UserSession.isLoggedIn = true
        UserSession.userRole = "teacher"
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@")
    }
}