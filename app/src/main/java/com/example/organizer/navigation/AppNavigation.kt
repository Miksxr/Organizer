package com.example.organizer.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.organizer.presentation.screens.grades.GradesScreen
import com.example.organizer.presentation.screens.homeworks.HomeworksScreen
import com.example.organizer.presentation.screens.subjects.SubjectsScreen
import com.example.organizer.presentation.screens.settings.SettingsScreen
import com.example.organizer.presentation.screens.settings.SettingsViewModel
import com.example.organizer.presentation.screens.settings.ThemeMode
import com.example.organizer.presentation.theme.OrganizerTheme

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val themeMode by settingsViewModel.themeState.collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM.name)

    OrganizerTheme(themeMode = themeMode) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Subjects.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomNavItem.Subjects.route) {
                    SubjectsScreen()
                }

                composable(BottomNavItem.Homeworks.route) {
                    HomeworksScreen()
                }

                composable(BottomNavItem.Grades.route) {
                    GradesScreen()
                }

                composable(BottomNavItem.Settings.route) {
                    SettingsScreen()
                }
            }
        }
    }
}