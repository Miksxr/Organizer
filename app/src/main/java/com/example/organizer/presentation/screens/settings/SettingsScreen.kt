package com.example.organizer.presentation.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.organizer.R
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val themeMode by viewModel.themeState.collectAsState(initial = ThemeMode.SYSTEM.name)
    var showThemeDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Настройки темы", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        ListItem(
            headlineContent = { Text("Тема приложения") },
            supportingContent = { Text("Текущая тема: ${themeMode.lowercase()}") },
            trailingContent = { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null) },
            modifier = Modifier.clickable { showThemeDialog = true }
        )
    }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Выбор темы") },
            text = {
                Column {
                    ThemeMode.entries.forEach { mode ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Запускаем в корутине
                                    coroutineScope.launch {
                                        viewModel.setThemeMode(mode.name)
                                    }
                                    showThemeDialog = false
                                }
                                .padding(8.dp)
                        ) {
                            RadioButton(
                                selected = themeMode == mode.name,
                                onClick = null
                            )
                            Text(
                                text = when(mode) {
                                    ThemeMode.LIGHT -> "Дневная"
                                    ThemeMode.DARK -> "Ночная"
                                    ThemeMode.SYSTEM -> "Системная"
                                },
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}