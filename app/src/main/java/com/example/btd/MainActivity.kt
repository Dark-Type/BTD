package com.example.btd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.btd.View.AuthScreen
import com.example.btd.View.StudentScreen
import com.example.btd.View.TeacherScreen
import com.example.btd.ui.theme.BTDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BTDTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "auth") {
                        composable("auth") { AuthScreen(navController = navController) }
                        composable("teacher_home") { TeacherScreen() }
                        composable("student_home") { StudentScreen() }
                    }
                }
            }
        }
    }
}

