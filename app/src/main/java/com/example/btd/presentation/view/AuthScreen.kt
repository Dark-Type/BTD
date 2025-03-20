package com.example.btd.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.btd.data.models.FacultyModel
import com.example.btd.data.models.GroupModel
import com.example.btd.data.models.TokenResponse
import com.example.btd.domain.models.UiState
import com.example.btd.presentation.viewmodel.AuthViewModel

@Composable
fun AuthScreen(navController: NavController) {
    var authMode by remember { mutableStateOf("") }
    var userRole by remember { mutableStateOf("") }

    val authViewModel: AuthViewModel = viewModel()

    val loginState by authViewModel.loginState.observeAsState()
    val registerState by authViewModel.registerState.observeAsState()


    val facultiesState by authViewModel.faculties.collectAsState()
    val groupsState by authViewModel.groups.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.loadFaculties()
    }

    LaunchedEffect(loginState) {
        if (loginState is UiState.Success<TokenResponse>) {
            if (userRole == "teacher") navController.navigate("teacher_home")
            else navController.navigate("student_home")
        }
    }
    LaunchedEffect(registerState) {
        if (registerState is UiState.Success<TokenResponse>) {
            if (userRole == "teacher") navController.navigate("teacher_home")
            else navController.navigate("student_home")
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        when {
            authMode.isEmpty() -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(12.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(0.9f)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Welcome!", style = MaterialTheme.typography.labelLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { authMode = "login" },
                            modifier = Modifier.fillMaxWidth(),
                            colors = buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(text = "Login", color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { authMode = "register" },
                            modifier = Modifier.fillMaxWidth(),
                            colors = buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(text = "Register", color = Color.White)
                        }
                    }
                }
            }

            authMode.isNotEmpty() && userRole.isEmpty() -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(12.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(0.9f)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Proceed as:", style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { userRole = "teacher" },
                            modifier = Modifier.fillMaxWidth(),
                            colors = buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text(
                                text = if (authMode == "login") "Teacher Login" else "Teacher Registration",
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { userRole = "student" },
                            modifier = Modifier.fillMaxWidth(),
                            colors = buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text(
                                text = if (authMode == "login") "Student Login" else "Student Registration",
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "← Back",
                            modifier = Modifier.clickable { authMode = ""; userRole = "" },
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            else -> {
                when (authMode) {
                    "login" -> {
                        LoginForm(
                            userRole = userRole,
                            onLogin = { email, password ->
                                authViewModel.login(email, password, userRole)
                            },
                            errorMessage = if (loginState is UiState.Error<*>) (loginState as UiState.Error<*>).errorMessage else "",
                            onGoBack = { userRole = "" }
                        )
                    }

                    "register" -> {
                        if (userRole == "teacher") {
                            TeacherRegistrationForm(
                                onRegister = { name, surname, email, phoneNumber, password, verifyPassword ->
                                    authViewModel.registerTeacher(
                                        name,
                                        surname,
                                        email,
                                        phoneNumber,
                                        password,
                                        verifyPassword
                                    )
                                },
                                errorMessage = if (registerState is UiState.Error<*>) (registerState as UiState.Error<*>).errorMessage else "",
                                onGoBack = { userRole = "" }
                            )
                        } else {


                            StudentRegistrationForm(
                                faculties = when (facultiesState) {
                                    is UiState.Success -> (facultiesState as UiState.Success<List<FacultyModel>>).data
                                    else -> emptyList()
                                },
                                groups = when (groupsState) {
                                    is UiState.Success -> (groupsState as UiState.Success<List<GroupModel>>).data
                                    else -> emptyList()
                                },
                                onFacultySelected = { facultyId ->
                                    authViewModel.loadGroups(facultyId)
                                },
                                onRegister = { name, surname, patronymic, email, groups, phoneNumber, password, verifyPassword ->
                                    authViewModel.registerStudent(
                                        name = name,
                                        surname = surname,
                                        email = email,
                                        group = groups,
                                        password = password,
                                        verifyPassword = verifyPassword,
                                        patronymic = patronymic,
                                        phoneNumber = phoneNumber
                                    )
                                },
                                errorMessage = if (registerState is UiState.Error<*>) (registerState as UiState.Error<*>).errorMessage else "",
                                onGoBack = { userRole = "" },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginForm(
    userRole: String,
    onLogin: (String, String) -> Unit,
    errorMessage: String,
    onGoBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(0.9f)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (userRole == "teacher") "Teacher Login" else "Student Login",
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onLogin(email, password) },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Login", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "← Back",
                modifier = Modifier.clickable { onGoBack() },
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TeacherRegistrationForm(
    onRegister: (String, String, String, String, String, String) -> Unit,
    errorMessage: String,
    onGoBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var faculty by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verifyPassword by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(0.9f)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(text = "Teacher Registration", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Surname") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = faculty,
                onValueChange = { faculty = it },
                label = { Text("Phone number") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = verifyPassword,
                onValueChange = { verifyPassword = it },
                label = { Text("Verify Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onRegister(name, surname, email, faculty, password, verifyPassword) },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Register as Teacher", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "← Back",
                modifier = Modifier.clickable { onGoBack() },
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun StudentRegistrationForm(
    faculties: List<FacultyModel>,
    groups: List<GroupModel>,
    onFacultySelected: (String) -> Unit,
    onRegister: (String, String, String, String, List<String>, String?, String, String) -> Unit,
    errorMessage: String,
    onGoBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var patronymic by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verifyPassword by remember { mutableStateOf("") }

    var currentFaculty by remember { mutableStateOf<FacultyModel?>(null) }
    var selectedGroups by remember { mutableStateOf<List<GroupModel>>(emptyList()) }

    var showFacultyDialog by remember { mutableStateOf(false) }
    var showGroupDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(0.9f)
            .heightIn(max = 550.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Student Registration", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name*") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Surname*") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = patronymic,
                onValueChange = { patronymic = it },
                label = { Text("Patronymic (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email*") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number (Optional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showFacultyDialog = true },
                shape = RoundedCornerShape(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedGroups.isEmpty()) "Add Groups*" else "Groups (${selectedGroups.size})",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Add Groups"
                    )
                }
            }

            if (showFacultyDialog) {
                Dialog(onDismissRequest = { showFacultyDialog = false }) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .heightIn(max = 400.dp)
                        ) {
                            Text(
                                text = "Select Faculty",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            LazyColumn(
                                modifier = Modifier.weight(1f)
                            ) {
                                items(faculties) { faculty ->
                                    Text(
                                        text = faculty.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {

                                                currentFaculty = faculty
                                                onFacultySelected(faculty.id)


                                                showFacultyDialog = false
                                                showGroupDialog = true
                                            }
                                            .padding(vertical = 12.dp)
                                    )
                                }
                            }

                            Button(
                                onClick = { showFacultyDialog = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                colors = buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }

            if (showGroupDialog && currentFaculty != null) {
                Dialog(onDismissRequest = { showGroupDialog = false }) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .heightIn(max = 400.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Groups in ${currentFaculty?.name}",
                                    style = MaterialTheme.typography.labelMedium
                                )
                                IconButton(onClick = {
                                    showGroupDialog = false
                                    showFacultyDialog = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Back to faculties",
                                        modifier = Modifier.rotate(90f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            LazyColumn(
                                modifier = Modifier.weight(1f)
                            ) {
                                items(groups) { group ->
                                    val isSelected = selectedGroups.contains(group)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedGroups = if (isSelected) {
                                                    selectedGroups - group
                                                } else {
                                                    selectedGroups + group
                                                }
                                            }
                                            .padding(vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = group.number,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = { showGroupDialog = false },
                                    modifier = Modifier.weight(1f),
                                    colors = buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Text("Done")
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        showGroupDialog = false
                                        showFacultyDialog = true
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                ) {
                                    Text("Add More")
                                }
                            }
                        }
                    }
                }
            }

            if (selectedGroups.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Selected Groups:", style = MaterialTheme.typography.bodyMedium)

                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    selectedGroups.forEach { group ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = group.number,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Remove",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.clickable {
                                    selectedGroups = selectedGroups - group
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password*") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = verifyPassword,
                onValueChange = { verifyPassword = it },
                label = { Text("Verify Password*") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onRegister(
                        name,
                        surname,
                        patronymic,
                        email,
                        selectedGroups.map { it.id },
                        phoneNumber.ifBlank { null },
                        password,
                        verifyPassword
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                enabled = name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty() &&
                        selectedGroups.isNotEmpty() && password.isNotEmpty() && verifyPassword.isNotEmpty()
            ) {
                Text(text = "Register as Student", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "← Back",
                modifier = Modifier.clickable { onGoBack() },
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

