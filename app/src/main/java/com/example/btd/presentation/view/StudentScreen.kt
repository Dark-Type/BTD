package com.example.btd.presentation.view

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btd.data.models.AbsenceModel
import com.example.btd.presentation.viewmodel.StudentScreenViewModel
import com.example.btd.presentation.viewmodel.SubmissionResult
import com.example.btd.session.UserSession
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun StudentScreen(navController: NavController) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showSubmissionDialog by remember { mutableStateOf(false) }
    var expandedSubmissionId by remember { mutableStateOf<String?>(null) }
    val lazyListState = rememberLazyListState()
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var documentName by remember { mutableStateOf<String?>(null) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val context = LocalContext.current

    val viewModel: StudentScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val submissions by viewModel.submissions.collectAsState()


    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.submissionResult.collect { result ->
            when (result) {
                is SubmissionResult.Success -> {
                    showSubmissionDialog = false
                    startDate = null
                    endDate = null
                    documentName = null
                    Toast.makeText(context, "Submission successful", Toast.LENGTH_SHORT).show()
                }

                is SubmissionResult.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    var selectedDocumentUri by remember { mutableStateOf<Uri?>(null) }
    var multipartBodyPart by remember {
        mutableStateOf<List<MultipartBody.Part>>(emptyList())
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedDocumentUri = uri
            uri?.let { selectedUri ->
                try {
                    val inputStream = context.contentResolver.openInputStream(selectedUri)
                    inputStream?.use { stream ->
                        val byteArray = stream.readBytes()

                        val file = File.createTempFile("temp", null, context.cacheDir)
                        file.writeBytes(byteArray)

                        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        val body = MultipartBody.Part.createFormData(
                            "file",
                            file.name,
                            requestFile
                        )
                        multipartBodyPart = multipartBodyPart + body

                        val cursor =
                            context.contentResolver.query(selectedUri, null, null, null, null)
                        cursor?.use { c ->
                            if (c.moveToFirst()) {
                                val displayName =
                                    c.getString(c.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                                documentName = displayName
                            }
                        } ?: run {
                            documentName = selectedUri.lastPathSegment ?: "Unknown file"
                            Toast.makeText(
                                context,
                                "Could not get file details, using fallback name",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        context.contentResolver.takePersistableUriPermission(
                            selectedUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

                        Toast.makeText(
                            context,
                            "File selected: $documentName, size: ${byteArray.size} bytes",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    documentName = "Error getting file name"
                    Toast.makeText(
                        context,
                        "Error getting file details: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    )

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    UserSession.logout()
                    showLogoutDialog = false
                    navController.navigate("auth") {
                        popUpTo("auth") { inclusive = true }
                    }
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("No")
                }
            }
        )
    }


    if (showSubmissionDialog) {
        AlertDialog(
            onDismissRequest = { showSubmissionDialog = false },
            title = { Text("Submit Absence") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Start Date: ")
                        Text(text = startDate?.format(dateFormatter) ?: "Not selected")
                        Spacer(modifier = Modifier.padding(4.dp))
                        Button(onClick = { showStartDatePicker = true }) { Text("Select") }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "End Date: ")
                        Text(text = endDate?.format(dateFormatter) ?: "Not selected")
                        Spacer(modifier = Modifier.padding(4.dp))
                        Button(onClick = { showEndDatePicker = true }) { Text("Select") }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Document: ${documentName ?: "None"}")
                        Spacer(modifier = Modifier.padding(4.dp))
                        Button(onClick = { filePickerLauncher.launch("*/*") }) {
                            Text("Upload File")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (startDate == null || endDate == null) {
                            Toast.makeText(
                                context,
                                "Please select start and end dates.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.submitAbsence(startDate!!, endDate!!, "fucking reason", multipartBodyPart)
                        }
                    }
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showSubmissionDialog = false
                        startDate = null
                        endDate = null
                        documentName = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showStartDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                startDate = LocalDate.of(year, month + 1, dayOfMonth)
                showStartDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    if (showEndDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                endDate = LocalDate.of(year, month + 1, dayOfMonth)
                showEndDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student Home Screen") },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            viewModel.refreshSubmissions()
                        }
                    }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh")
                    }
                },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(submissions.size) { index ->
                            val submission = submissions[index]
                            SubmissionCard(
                                submission = submission,
                                isExpanded = (expandedSubmissionId == submission.id),
                                onClick = {
                                    expandedSubmissionId =
                                        if (expandedSubmissionId == submission.id) null else submission.id
                                }
                            )
                        }

                        item {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(16.dp)
                            )
                        }
                    }

                    Button(
                        onClick = { showSubmissionDialog = true },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text("I will be missing on...")
                    }
                }
            }
        }
    )
}

@Composable
fun SubmissionCard(submission: AbsenceModel, isExpanded: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                "From: ${submission.from} To: ${submission.to}",
                fontWeight = FontWeight.Bold
            )
            Text("Status: ${submission.status}")
            if (isExpanded) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = "Document: ${submission.files ?: "No document attached"}",
                    color = Color.Gray
                )
            }
        }
    }
}