package com.example.btd.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btd.presentation.models.AttendanceRecord
import com.example.btd.services.MockAttendanceService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class TeacherViewModel : ViewModel() {
    private val _attendanceRecords = MutableStateFlow<List<AttendanceRecord>>(emptyList())
    val attendanceRecords: StateFlow<List<AttendanceRecord>> get() = _attendanceRecords

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        loadAttendanceData()
    }

    private fun loadAttendanceData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                delay(500)
                val now = LocalDate.now()
                _attendanceRecords.value = MockAttendanceService.getAttendanceData(now.monthValue, now.year)
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun refreshAttendanceData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                delay(500)
                val now = LocalDate.now()
                _attendanceRecords.value = MockAttendanceService.getAttendanceData(now.monthValue, now.year)
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}