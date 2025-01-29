package com.example.organizer.presentation.screens.grades

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.organizer.R

@Composable
fun GradesScreen() {
    Image(
        painter = painterResource(id = R.drawable.leo), // Изображение (painterResource(id = ))
        contentDescription = "Назад", // Описание изображения
    )
}