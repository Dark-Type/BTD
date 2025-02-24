package com.example.btd.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btd.models.AttendanceRecord
import com.example.btd.viewmodel.TeacherViewModel
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun TeacherAttendanceScreen() {
    val viewModel: TeacherViewModel = viewModel()
    val attendanceRecords by viewModel.attendanceRecords.collectAsState()

    val currentDate = LocalDate.now()
    val yearMonth = YearMonth.of(currentDate.year, currentDate.month)
    val daysInMonth = yearMonth.lengthOfMonth()
    val monthDates =
        (1..daysInMonth).map { day -> LocalDate.of(currentDate.year, currentDate.month, day) }

    var selectedRecord by remember { mutableStateOf<AttendanceRecord?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Attendance Calendar", fontWeight = FontWeight.Bold)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray)
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(monthDates) { date ->

                val record = attendanceRecords.find { it.date == date }
                val backgroundColor =
                    if (record != null && record.absences.isNotEmpty()) Color(0xFFFFCDD2) else Color(
                        0xFFE0E0E0
                    )
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(backgroundColor)
                        .clickable {
                            if (record != null && record.absences.isNotEmpty()) {
                                selectedRecord = record
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = date.dayOfMonth.toString(), modifier = Modifier.padding(8.dp))
                }
            }
        }
    }


    selectedRecord?.let { record ->
        AlertDialog(
            onDismissRequest = { selectedRecord = null },
            title = { Text(text = "Absences on ${record.date}") },
            text = {
                Column {
                    val withoutProof = record.absences.filter { !it.hasProof }
                    val withProof = record.absences.filter { it.hasProof }

                    if (withoutProof.isNotEmpty()) {
                        Text(text = "Without Proof:", fontWeight = FontWeight.Bold)
                        withoutProof.forEach { absence ->
                            Text(
                                text = absence.name,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    if (withProof.isNotEmpty()) {
                        Text(
                            text = "With Proof:",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        withProof.forEach { absence ->
                            Text(
                                text = absence.name,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { selectedRecord = null }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                    Text(text = "Close", modifier = Modifier.padding(start = 4.dp))
                }
            }
        )
    }
}