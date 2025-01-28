package com.example.organizer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.organizer.presentation.screens.subjects.SubjectScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "subjects") {
        composable("subjects") {
            SubjectScreen()
        }
        // Добавьте другие экраны
    }
}