package com.example.btd.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btd.data.models.FacultyModel
import com.example.btd.data.models.GroupModel
import com.example.btd.data.models.LoginModel
import com.example.btd.data.models.RegisterStudentModel
import com.example.btd.data.models.RegisterUserModel
import com.example.btd.data.models.TokenResponse
import com.example.btd.domain.TokenManager
import com.example.btd.domain.converters.GetFacultyConverter
import com.example.btd.domain.converters.GetGroupConverter
import com.example.btd.domain.converters.LoginConverter
import com.example.btd.domain.converters.RegisterConverter
import com.example.btd.domain.converters.RegisterUserConverter
import com.example.btd.domain.models.UiState
import com.example.btd.domain.use_cases.GetFacultyUseCase
import com.example.btd.domain.use_cases.GetGroupsUseCase
import com.example.btd.domain.use_cases.LoginUseCase
import com.example.btd.domain.use_cases.RegisterUseCase
import com.example.btd.domain.use_cases.RegisterUserUseCase
import com.example.btd.session.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val loginUseCase = LoginUseCase()
    private val registerUseCase = RegisterUseCase()
    private val registerUserUseCase = RegisterUserUseCase()
    private val getFacultyUseCase = GetFacultyUseCase()
    private val getGroupsUseCase = GetGroupsUseCase()

    private val loginConverter = LoginConverter()
    private val registerConverter = RegisterConverter()
    private val registerUserConverter = RegisterUserConverter()
    private val getFacultyConverter = GetFacultyConverter()
    private val getGroupsConverter = GetGroupConverter()

    private val _loginState = MutableLiveData<UiState<TokenResponse>>()
    val loginState: LiveData<UiState<TokenResponse>> = _loginState
    private val _faculties = MutableStateFlow<UiState<List<FacultyModel>>>(UiState.Loading)
    val faculties: StateFlow<UiState<List<FacultyModel>>> = _faculties

    private val _groups = MutableStateFlow<UiState<List<GroupModel>>>(UiState.Loading)
    val groups: StateFlow<UiState<List<GroupModel>>> = _groups

    private val _registerState = MutableLiveData<UiState<TokenResponse>>()
    val registerState: LiveData<UiState<TokenResponse>> = _registerState

    fun loadFaculties() {
        viewModelScope.launch {
            getFacultyUseCase.execute(GetFacultyUseCase.Request())
                .map { getFacultyConverter.convert(it) }
                .collect { _faculties.value = it }
        }
    }

    fun loadGroups(facultyId: String) {
        viewModelScope.launch {
            getGroupsUseCase.execute(GetGroupsUseCase.Request(facultyId))
                .map { getGroupsConverter.convert(it) }
                .collect { _groups.value = it }
        }
    }

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
                    Log.d("token",uiState.data.token)
                    TokenManager.getInstance().saveToken(uiState.data.token)
                }
            }
        }
    }


    fun registerStudent(
        name: String,
        surname: String,
        email: String,
        group: List<String>,
        password: String,
        verifyPassword: String,
        patronymic: String = "",
        phoneNumber: String? = null,
    ) {
        if (name.isBlank() || surname.isBlank() || !isValidEmail(email)  || group.isEmpty() || password.isBlank() || verifyPassword.isBlank()) {
            _registerState.value =
                UiState.Error("Please fill out all required fields with valid values.")
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
                    surname, name, patronymic, email, password, phoneNumber ?: "", group
                )
            )

            registerUseCase.execute(request).collect { result ->
                val uiState = registerConverter.convert(result)
                _registerState.value = uiState
                Log.d("AuthViewModel", "Registration result: $uiState")


                if (uiState is UiState.Success) {
                    UserSession.isLoggedIn = true
                    UserSession.userRole = "student"
                    TokenManager.getInstance().saveToken(uiState.data.token)
                }
            }
        }
    }

    fun registerTeacher(
        name: String,
        surname: String,
        email: String,
        phoneNumber: String,
        password: String,
        verifyPassword: String,
        patronymic: String = "",
    ) {
        if (name.isBlank() || surname.isBlank() || !isValidEmail(email) || phoneNumber.isBlank() || password.isBlank() || verifyPassword.isBlank()) {
            _registerState.value =
                UiState.Error("Please fill out all required fields with valid values.")
            return
        }

        if (password != verifyPassword) {
            _registerState.value = UiState.Error("Passwords do not match.")
            return
        }
        viewModelScope.launch {

            _registerState.value = UiState.Loading


            val request = RegisterUserUseCase.Request(
                RegisterUserModel(
                    surname, name, patronymic, email, password, phoneNumber
                )
            )

            registerUserUseCase.execute(request).collect { result ->
                val uiState = registerUserConverter.convert(result)
                _registerState.value = uiState
                Log.d("AuthViewModel", "Registration result: $uiState")


                if (uiState is UiState.Success) {
                    UserSession.isLoggedIn = true
                    UserSession.userRole = "student"
                    TokenManager.getInstance().saveToken(uiState.data.token)
                }
            }
        }

        _registerState.value = UiState.Success(TokenResponse("teacher-mock-token"))
        UserSession.isLoggedIn = true
        UserSession.userRole = "teacher"
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@")
    }
}