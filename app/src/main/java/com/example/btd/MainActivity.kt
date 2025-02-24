package com.example.btd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.btd.session.UserSession
import com.example.btd.view.AuthScreen
import com.example.btd.view.StudentScreen
import com.example.btd.view.TeacherScreen
import com.example.btd.ui.theme.BTDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserSession.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            BTDTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val startDestination = if (UserSession.isLoggedIn) {
                        if (UserSession.userRole == "teacher") "teacher_home" else "student_home"
                    } else {
                        "auth"
                    }
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("auth") {
                            AuthScreen(navController = navController)
                        }
                        composable("teacher_home") {
                            TeacherScreen(navController = navController)
                        }
                        composable("student_home") {
                            StudentScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
