package com.example.btd.View

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AuthScreen(navController: NavController) {

    var authMode by remember { mutableStateOf("") }

    var userRole by remember { mutableStateOf("") }

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
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(0.9f)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Welcome!", style = MaterialTheme.typography.labelLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { authMode = "login" },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(text = "Login", color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { authMode = "register" },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
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
                        Text(
                            text = "Proceed as:",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { userRole = "teacher" },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
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
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
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

                        LoginForm(userRole = userRole, onLoginSuccess = {
                            if (userRole == "teacher") navController.navigate("teacher_home")
                            else navController.navigate("student_home")
                        })
                    }

                    "register" -> {

                        if (userRole == "teacher") {
                            TeacherRegistrationForm(onSubmit = {
                                navController.navigate("teacher_home")
                            })
                        } else {
                            StudentRegistrationForm(onSubmit = {
                                navController.navigate("student_home")
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginForm(userRole: String, onLoginSuccess: () -> Unit) {
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onLoginSuccess() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Login", color = Color.White)
            }
        }
    }
}

@Composable
fun TeacherRegistrationForm(onSubmit: () -> Unit) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var extraInfo by remember { mutableStateOf("") }

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
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Subject") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = extraInfo,
                onValueChange = { extraInfo = it },
                label = { Text("Extra Info") })
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Register as Teacher", color = Color.White)
            }
        }
    }
}

@Composable
fun StudentRegistrationForm(onSubmit: () -> Unit) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var graduationYear by remember { mutableStateOf("") }
    var major by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var extraInfo by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(0.9f)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(text = "Student Registration", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = graduationYear,
                onValueChange = { graduationYear = it },
                label = { Text("Graduation Year") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = major, onValueChange = { major = it }, label = { Text("Major") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = extraInfo,
                onValueChange = { extraInfo = it },
                label = { Text("Extra Info") })
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Register as Student", color = Color.White)
            }
        }
    }
}