package com.example.btd.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider

import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btd.presentation.models.AttendanceRecord
import com.example.btd.presentation.viewmodel.TeacherViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

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
        Text(
            text = "${currentDate.month.name} ${currentDate.year}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray)
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            items(7) { index ->
                val dayOfWeek = java.time.DayOfWeek.of(index + 1)
                Text(
                    text = dayOfWeek.name.take(3),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp),
                    textAlign = TextAlign.Center
                )
            }


            val firstDayOfMonth = LocalDate.of(currentDate.year, currentDate.month, 1)
            val dayOfWeekValue = firstDayOfMonth.dayOfWeek.value
            items(dayOfWeekValue - 1) {
                Box(modifier = Modifier.padding(4.dp))
            }


            items(monthDates) { date ->
                val record = attendanceRecords.find { it.date == date }
                val absenceCount = record?.absences?.size ?: 0

                val backgroundColor = when {
                    absenceCount > 0 -> Color(0xFFFFCDD2)
                    date == currentDate -> Color(0xFFE3F2FD)
                    else -> Color(0xFFE0E0E0)
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(backgroundColor, shape = RoundedCornerShape(4.dp))
                        .clickable(enabled = absenceCount > 0) {
                            if (absenceCount > 0) {
                                selectedRecord = record
                            }
                        }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            fontWeight = if (date == currentDate) FontWeight.Bold else FontWeight.Normal
                        )
                        if (absenceCount > 0) {
                            Text(
                                text = "$absenceCount",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
    selectedRecord?.let { record ->
        AlertDialog(
            onDismissRequest = { selectedRecord = null },
            title = {
                Text(
                    text = "Absences on ${record.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}"
                )
            },
            text = {
                Column {
                    val withoutProof = record.absences.filter { !it.hasProof }
                    val withProof = record.absences.filter { it.hasProof }

                    if (withoutProof.isNotEmpty()) {
                        Text(text = "Without Proof:", fontWeight = FontWeight.Bold)
                        withoutProof.forEach { absence ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp, top = 8.dp, bottom = 4.dp)
                            ) {
                                Text(
                                    text = absence.name,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Group: ${absence.group}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    if (withProof.isNotEmpty()) {
                        Text(
                            text = "With Proof:",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        withProof.forEach { absence ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp, top = 8.dp, bottom = 4.dp)
                            ) {
                                Text(
                                    text = absence.name,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Group: ${absence.group}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { selectedRecord = null }) {
                    Text(text = "Close")
                }
            }
        )
    }
}