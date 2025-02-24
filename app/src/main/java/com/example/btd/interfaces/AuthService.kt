package com.example.btd.interfaces

interface AuthService {
    fun login(email: String, password: String, role: String): Boolean
    fun registerTeacher(
        name: String,
        surname: String,
        email: String,
        faculty: String,
        password: String
    ): Boolean

    fun registerStudent(
        name: String,
        surname: String,
        email: String,
        faculty: String,
        group: String,
        password: String
    ): Boolean
}