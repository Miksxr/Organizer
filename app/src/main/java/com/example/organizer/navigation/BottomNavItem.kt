package com.example.organizer.navigation

import com.example.organizer.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int
) {
    data object Subjects : BottomNavItem(
        route = "subjects",
        title = "Предметы",
        icon = R.drawable.icon_subjects
    )

    data object Homeworks : BottomNavItem(
        route = "homeworks",
        title = "Домашка",
        icon = R.drawable.icon_homework
    )

    data object Grades : BottomNavItem(
        route = "grades",
        title = "Оценки",
        icon = R.drawable.icon_grades
    )

    data object Settings : BottomNavItem(
        route = "settings",
        title = "Настройки",
        icon = R.drawable.icon_settings
    )
}