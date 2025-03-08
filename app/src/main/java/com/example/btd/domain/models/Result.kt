package com.example.btd.domain.models

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    class Error(val exception: String) : Result<Nothing>()
}