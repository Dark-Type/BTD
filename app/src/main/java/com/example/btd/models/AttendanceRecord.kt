package com.example.btd.models

import java.time.LocalDate

data class AttendanceRecord(
    val date: LocalDate,
    val absences: List<StudentAbsence>
)
