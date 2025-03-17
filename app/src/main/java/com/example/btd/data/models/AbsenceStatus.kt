package com.example.btd.data.models

import kotlinx.serialization.Serializable

@Serializable
enum class AbsenceStatus {
    Checking, Approved, Rejected
}