package com.example.organizer.presentation.screens.settings

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.organizer.R
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val themeMode by viewModel.themeState.collectAsState(initial = ThemeMode.SYSTEM.name)
    var showThemeDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState(initial = true)

    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Настройки",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Настройка темы
        Box(
            modifier = Modifier
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp)
        ) {
            ListItem(
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_night),
                        contentDescription = "Тема",
                        modifier = Modifier.size(24.dp))
                },
                headlineContent = { Text("Тема приложения", fontSize = 16.sp) },
                supportingContent = {
                    Text(
                        "Текущая тема: ${themeMode.lowercase()}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                },
                trailingContent = {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Перейти",
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier.clickable { showThemeDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Настройка уведомлений
        Box(
            modifier = Modifier
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp)
        ) {
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Filled.Notifications,
                        contentDescription = "Уведомления",
                        modifier = Modifier.size(24.dp))
                },
                headlineContent = { Text("Уведомления", fontSize = 16.sp) },
                supportingContent = {
                    Text(
                        "Включить напоминания",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                },
                trailingContent = {
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = {
                            coroutineScope.launch {
                                viewModel.setNotificationsEnabled(it)
                            }
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            themeMode = themeMode,
            onThemeSelected = { mode ->
                coroutineScope.launch {
                    viewModel.setThemeMode(mode.name)
                }
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }
}

@Composable
private fun ThemeSelectionDialog(
    themeMode: String,
    onThemeSelected: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выбор темы") },
        text = {
            Column {
                ThemeMode.entries.forEach { mode ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onThemeSelected(mode) }
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
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        }
    )
}

class NotificationHelper(private val context: Context) {
    companion object {
        const val CHANNEL_ID = "deadline_channel"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Напоминания о дедлайнах",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления о приближающихся дедлайнах"
                enableLights(true)
            }

            val notificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(title: String, message: String) {
        // Проверка разрешения для Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.w("Notification", "Permission not granted")
                return
            }
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_subjects)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(getUniqueId(), notification)
    }

    private fun getUniqueId(): Int {
        return System.currentTimeMillis().toInt()
    }
}