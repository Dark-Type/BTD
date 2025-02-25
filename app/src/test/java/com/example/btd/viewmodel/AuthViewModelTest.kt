package com.example.btd.viewmodel

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.btd.session.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class AuthViewModelTest {

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AuthViewModel
    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)


        MockitoAnnotations.initMocks(this)


        prefs = Mockito.mock(SharedPreferences::class.java)
        editor = Mockito.mock(SharedPreferences.Editor::class.java)


        Mockito.`when`(prefs.edit()).thenReturn(editor)
        Mockito.`when`(editor.putBoolean(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(editor)
        Mockito.`when`(editor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(editor)
        Mockito.`when`(prefs.getBoolean(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(false)
        Mockito.`when`(prefs.getString(Mockito.anyString(), Mockito.anyString())).thenReturn("")


        UserSession.prefs = prefs

        viewModel = AuthViewModel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()

        Mockito.reset(prefs, editor)
    }

    @Test
    fun testLoginSuccess() = runTest(testDispatcher) {
        viewModel.login("teacher@example.com", "password", "teacher")
        testDispatcher.scheduler.advanceUntilIdle()
        val result = viewModel.loginState.value
        assertTrue("Expected login success", result is AuthViewModel.LoginResult.Success)
    }

    @Test
    fun testLoginFailureInvalidEmail() = runTest(testDispatcher) {
        viewModel.login("invalid-email", "password", "teacher")
        testDispatcher.scheduler.advanceUntilIdle()
        val result = viewModel.loginState.value
        assertTrue("Expected login error", result is AuthViewModel.LoginResult.Error)
        if (result is AuthViewModel.LoginResult.Error) {
            assertEquals("Enter a valid email and password cannot be empty.", result.message)
        }
    }

    @Test
    fun testLoginFailureEmptyPassword() = runTest(testDispatcher) {
        viewModel.login("teacher@example.com", "", "teacher")
        testDispatcher.scheduler.advanceUntilIdle()
        val result = viewModel.loginState.value
        assertTrue("Expected login error", result is AuthViewModel.LoginResult.Error)
        if (result is AuthViewModel.LoginResult.Error) {
            assertEquals("Enter a valid email and password cannot be empty.", result.message)
        }
    }

    @Test
    fun testTeacherRegistrationPasswordMismatch() = runTest(testDispatcher) {
        viewModel.registerTeacher("John", "Doe", "john.doe@example.com", "Science", "pass123", "pass124")
        testDispatcher.scheduler.advanceUntilIdle()
        val result = viewModel.registerState.value
        assertTrue("Expected teacher registration error", result is AuthViewModel.RegisterResult.Error)
        if (result is AuthViewModel.RegisterResult.Error) {
            assertEquals("Passwords do not match.", result.message)
        }
    }

    @Test
    fun testTeacherRegistrationMissingFields() = runTest(testDispatcher) {
        viewModel.registerTeacher("", "Doe", "john.doe@example.com", "Science", "pass123", "pass123")
        testDispatcher.scheduler.advanceUntilIdle()
        val result = viewModel.registerState.value
        assertTrue("Expected teacher registration error", result is AuthViewModel.RegisterResult.Error)
        if (result is AuthViewModel.RegisterResult.Error) {
            assertEquals("Please fill out all required fields with valid values.", result.message)
        }
    }

    @Test
    fun testTeacherRegistrationSuccess() = runTest(testDispatcher) {
        viewModel.registerTeacher("John", "Doe", "john.doe@example.com", "Science", "pass123", "pass123")
        testDispatcher.scheduler.advanceUntilIdle()
        val result = viewModel.registerState.value
        assertTrue("Expected teacher registration success", result is AuthViewModel.RegisterResult.Success)
    }

    @Test
    fun testStudentRegistrationPasswordMismatch() = runTest(testDispatcher) {
        viewModel.registerStudent("Jane", "Smith", "jane.smith@example.com", "Engineering", "Group A", "pass123", "pass124")
        testDispatcher.scheduler.advanceUntilIdle()
        val result = viewModel.registerState.value
        assertTrue("Expected student registration error", result is AuthViewModel.RegisterResult.Error)
        if (result is AuthViewModel.RegisterResult.Error) {
            assertEquals("Passwords do not match.", result.message)
        }
    }

    @Test
    fun testStudentRegistrationMissingFields() = runTest(testDispatcher) {
        viewModel.registerStudent("Jane", "", "jane.smith@example.com", "Engineering", "Group A", "pass123", "pass123")
        testDispatcher.scheduler.advanceUntilIdle()
        val result = viewModel.registerState.value
        assertTrue("Expected student registration error", result is AuthViewModel.RegisterResult.Error)
        if (result is AuthViewModel.RegisterResult.Error) {
            assertEquals("Please fill out all required fields with valid values.", result.message)
        }
    }

    @Test
    fun testStudentRegistrationSuccess() = runTest(testDispatcher) {
        viewModel.registerStudent("Jane", "Smith", "jane.smith@example.com", "Engineering", "Group A", "pass123", "pass123")
        testDispatcher.scheduler.advanceUntilIdle()
        val result = viewModel.registerState.value
        assertTrue("Expected student registration success", result is AuthViewModel.RegisterResult.Success)
    }
}