package com.example.btd.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btd.models.AttendanceRecord
import com.example.btd.services.MockAttendanceService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class TeacherViewModel : ViewModel() {
    private val _attendanceRecords = MutableStateFlow<List<AttendanceRecord>>(emptyList())
    val attendanceRecords: StateFlow<List<AttendanceRecord>> get() = _attendanceRecords

    init {
        loadAttendanceData()
    }

    private fun loadAttendanceData() {
        viewModelScope.launch {
            delay(500)
            val now = LocalDate.now()
            _attendanceRecords.value = MockAttendanceService.getAttendanceData(now.monthValue, now.year)
        }
    }

//    fun getRecordForDate(date: LocalDate): AttendanceRecord? {
//        return _attendanceRecords.value.find { it.date == date }
//    }
}