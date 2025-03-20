package com.example.btd.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
                            Text(text = if (authMode == "login") "Teacher Login" else "Teacher Registration", color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { userRole = "student" },
                            modifier = Modifier.fillMaxWidth(),
                            colors = buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text(text = if (authMode == "login") "Student Login" else "Student Registration", color = Color.White)
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
                                    authViewModel.registerTeacher(name, surname, email, phoneNumber, password, verifyPassword)
                                },
                                errorMessage = if (registerState is UiState.Error<*>) (registerState as UiState.Error<*>).errorMessage else "",
                                onGoBack = { userRole = "" }
                            )
                        } else {
                            UpdatedStudentRegistrationForm(
                                onRegister = { name, surname, patronymic, email, faculty, groups, phoneNumber, password, verifyPassword ->
                                    authViewModel.registerStudent(
                                        name = name,
                                        surname = surname,
                                        email = email,
                                        faculty = faculty,
                                        group = groups,
                                        password = password,
                                        verifyPassword = verifyPassword,
                                        patronymic = patronymic,
                                        phoneNumber = phoneNumber
                                    )
                                },
                                errorMessage = if (registerState is UiState.Error<*>) (registerState as UiState.Error<*>).errorMessage else "",
                                onGoBack = { userRole = "" }
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
            TextField(value = surname, onValueChange = { surname = it }, label = { Text("Surname") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = faculty, onValueChange = { faculty = it }, label = { Text("Phone number") })
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
fun UpdatedStudentRegistrationForm(
    onRegister: (String, String, String, String, String, List<String>, String?, String, String) -> Unit,
    errorMessage: String,
    onGoBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var patronymic by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var faculty by remember { mutableStateOf("") }

    var currentGroup by remember { mutableStateOf("") }
    var groups by remember { mutableStateOf(listOf<String>()) }

    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verifyPassword by remember { mutableStateOf("") }

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
                value = faculty,
                onValueChange = { faculty = it },
                label = { Text("Faculty*") },
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


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = currentGroup,
                    onValueChange = { currentGroup = it },
                    label = { Text("Group*") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (currentGroup.isNotEmpty() && !groups.contains(currentGroup)) {
                            groups = groups + currentGroup
                            currentGroup = ""
                        }
                    },
                    colors = buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Add")
                }
            }
            if (groups.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Added Groups:", style = MaterialTheme.typography.bodyMedium)
                groups.forEachIndexed { index, group ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = group,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Remove",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.clickable {
                                groups = groups.filterIndexed { i, _ -> i != index }
                            }
                        )
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
                        faculty,
                        if (groups.isEmpty() && currentGroup.isNotEmpty()) listOf(currentGroup) else groups,
                        if (phoneNumber.isBlank()) null else phoneNumber,
                        password,
                        verifyPassword
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                enabled = name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty() &&
                        faculty.isNotEmpty() && (groups.isNotEmpty() || currentGroup.isNotEmpty()) &&
                        password.isNotEmpty() && verifyPassword.isNotEmpty()
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