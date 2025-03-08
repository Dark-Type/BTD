package com.example.btd.services

import com.example.btd.presentation.models.AttendanceRecord
import com.example.btd.presentation.models.StudentAbsence
import java.time.LocalDate

object MockAttendanceService {
    fun getAttendanceData(currentMonth: Int, currentYear: Int): List<AttendanceRecord> {
        val record1 = AttendanceRecord(
            date = LocalDate.of(currentYear, currentMonth, 5),
            absences = listOf(
                StudentAbsence("Alice Johnson", hasProof = false),
                StudentAbsence("Bob Smith", hasProof = true)
            )
        )
        val record2 = AttendanceRecord(
            date = LocalDate.of(currentYear, currentMonth, 12),
            absences = listOf(
                StudentAbsence("Charlie Brown", hasProof = false)
            )
        )
        val record3 = AttendanceRecord(
            date = LocalDate.of(currentYear, currentMonth, 20),
            absences = listOf(
                StudentAbsence("Daisy Ridley", hasProof = true),
                StudentAbsence("Evan Peters", hasProof = false),
                StudentAbsence("Fiona Gallagher", hasProof = true)
            )
        )
        return listOf(record1, record2, record3)
    }
}