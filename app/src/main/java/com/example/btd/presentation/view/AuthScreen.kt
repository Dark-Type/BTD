package com.example.btd.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.btd.presentation.viewmodel.AuthViewModel

@Composable
fun AuthScreen(navController: NavController) {
    var authMode by remember { mutableStateOf("") }
    var userRole by remember { mutableStateOf("") }

    val authViewModel: AuthViewModel = viewModel()

    val loginState by authViewModel.loginState.collectAsState()
    val registerState by authViewModel.registerState.collectAsState()


    LaunchedEffect(loginState) {
        if (loginState is AuthViewModel.LoginResult.Success) {
            if (userRole == "teacher") navController.navigate("teacher_home")
            else navController.navigate("student_home")
        }
    }
    LaunchedEffect(registerState) {
        if (registerState is AuthViewModel.RegisterResult.Success) {
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
                    elevation = CardDefaults.cardElevation( 12.dp),
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
                    elevation = CardDefaults.cardElevation( 12.dp),
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
                            errorMessage = if (loginState is AuthViewModel.LoginResult.Error) (loginState as AuthViewModel.LoginResult.Error).message else "",
                            onGoBack = { userRole = "" }
                        )
                    }
                    "register" -> {
                        if (userRole == "teacher") {
                            TeacherRegistrationForm(
                                onRegister = { name, surname, email, faculty, password, verifyPassword ->
                                    authViewModel.registerTeacher(name, surname, email, faculty, password, verifyPassword)
                                },
                                errorMessage = if (registerState is AuthViewModel.RegisterResult.Error) (registerState as AuthViewModel.RegisterResult.Error).message else "",
                                onGoBack = { userRole = "" }
                            )
                        } else {
                            StudentRegistrationForm(
                                onRegister = { name, surname, email, faculty, group, password, verifyPassword ->
                                    authViewModel.registerStudent(/*name, surname, email, faculty, listOf (
                                        group), password, verifyPassword*/)
                                },
                                errorMessage = if (registerState is AuthViewModel.RegisterResult.Error) (registerState as AuthViewModel.RegisterResult.Error).message else "",
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
        elevation = CardDefaults.cardElevation( 12.dp),
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
            TextField(value = faculty, onValueChange = { faculty = it }, label = { Text("Faculty") })
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
    onRegister: (String, String, String, String, String, String, String) -> Unit,
    errorMessage: String,
    onGoBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var faculty by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verifyPassword by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation( 12.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(0.9f)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(text = "Student Registration", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = surname, onValueChange = { surname = it }, label = { Text("Surname") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = faculty, onValueChange = { faculty = it }, label = { Text("Faculty") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = group, onValueChange = { group = it }, label = { Text("Group") })
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
                onClick = { onRegister(name, surname, email, faculty, group, password, verifyPassword) },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Register as Student", color = Color.White)
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