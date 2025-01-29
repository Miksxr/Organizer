package com.example.organizer.presentation.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.organizer.R

@Composable
fun SettingsScreen() {
    Image(
        painter = painterResource(id = R.drawable.kep), // Изображение (painterResource(id = ))
        contentDescription = "Назад", // Описание изображения
        modifier = Modifier.fillMaxSize()
    )
}