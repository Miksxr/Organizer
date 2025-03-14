package com.example.organizer.presentation.theme

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.organizer.R
import com.example.organizer.presentation.screens.settings.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = OrangeDark,
    onPrimary = Orange,
    secondary = Orange,
    onSecondary = OrangeDark,
    tertiary = Gray
)

@Composable
fun OrganizerTheme(
    themeMode: String = ThemeMode.SYSTEM.name,
    content: @Composable () -> Unit
) {
    val systemDarkTheme = isSystemInDarkTheme()
    val darkTheme = when(themeMode) {
        ThemeMode.DARK.name -> true
        ThemeMode.LIGHT.name -> false
        else -> systemDarkTheme
    }

    val backgroundImage = if (darkTheme) R.drawable.bg_night else R.drawable.bg_day
    val imageAlpha = if (darkTheme) 0.15f else 0.1f // Разная прозрачность для тем

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                // Фоновое изображение
                Image(
                    painter = painterResource(backgroundImage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = imageAlpha,
                    modifier = Modifier.fillMaxSize()
                )

                // Основной контент
                content()
            }
        }
    )
}