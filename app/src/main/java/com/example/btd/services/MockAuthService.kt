package com.example.btd.services

import com.example.btd.interfaces.AuthService

class MockAuthService : AuthService {
    override fun login(email: String, password: String, role: String): Boolean {
        return email.contains("@") && password.isNotEmpty()
    }

    override fun registerTeacher(
        name: String,
        surname: String,
        email: String,
        faculty: String,
        password: String
    ): Boolean {
        return name.isNotBlank() &&
                surname.isNotBlank() &&
                email.contains("@") &&
                faculty.isNotBlank() &&
                password.isNotBlank()
    }

    override fun registerStudent(
        name: String,
        surname: String,
        email: String,
        faculty: String,
        group: String,
        password: String
    ): Boolean {

        return name.isNotBlank() &&
                surname.isNotBlank() &&
                email.contains("@") &&
                faculty.isNotBlank() &&
                group.isNotBlank() &&
                password.isNotBlank()
    }
}