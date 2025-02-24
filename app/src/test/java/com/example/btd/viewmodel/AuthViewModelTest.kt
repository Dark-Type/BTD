package com.example.btd.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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

class AuthViewModelTest {

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AuthViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
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
            assertEquals("Please fill out all fields with valid values.", result.message)
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