package com.example.organizer.presentation.screens.homeworks

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizer.AlarmReceiver
import com.example.organizer.data.local.entity.HomeworkEntity
import com.example.organizer.domain.repository.HomeworkRepository
import com.example.organizer.domain.usecase.AddHomeworkUseCase
import com.example.organizer.domain.usecase.DeleteHomeworkUseCase
import com.example.organizer.domain.usecase.GetHomeworksUseCase
import com.example.organizer.domain.usecase.UpdateHomeworkUseCase
import com.example.organizer.presentation.screens.settings.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeworksViewModel @Inject constructor(
    private val getHomeworksUseCase: GetHomeworksUseCase,
    private val addHomeworkUseCase: AddHomeworkUseCase,
    private val updateHomeworkUseCase: UpdateHomeworkUseCase,
    private val deleteHomeworkUseCase: DeleteHomeworkUseCase,
    private val repository: HomeworkRepository,
    private val notificationHelper: NotificationHelper,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _homeworks = MutableStateFlow<List<HomeworkEntity>>(emptyList())
    val homeworks: StateFlow<List<HomeworkEntity>> = _homeworks

    fun loadHomeworksForSubject(subjectId: Int) {
        viewModelScope.launch {
            getHomeworksUseCase(subjectId).collect {
                _homeworks.value = it
            }
        }
    }

    fun addHomework(title: String, description: String, dueDate: String, subjectId: Int) {
        viewModelScope.launch {
            val homework = HomeworkEntity(
                title = title,
                description = description,
                dueDate = dueDate,
                subjectId = subjectId
            )
            repository.addHomework(homework)
            scheduleNotification(homework)
        }
    }

    private fun scheduleNotification(homework: HomeworkEntity) {
        try {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val dueDate = dateFormat.parse(homework.dueDate) ?: return

            val alarmTime = dueDate.time - TimeUnit.DAYS.toMillis(1)

            if (alarmTime > System.currentTimeMillis()) {
                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    putExtra("TITLE", homework.title)
                    putExtra("MESSAGE", "До дедлайна по '${homework.title}' остался 1 день!")
                }

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    homework.id.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    alarmTime,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            Log.e("Notification", "Error scheduling notification", e)
        }
    }

    fun toggleHomeworkCompletion(homework: HomeworkEntity) {
        viewModelScope.launch {
            val updatedHomework = homework.copy(isCompleted = !homework.isCompleted)
            updateHomeworkUseCase(updatedHomework)
        }
    }

    fun deleteHomework(homework: HomeworkEntity) {
        viewModelScope.launch {
            deleteHomeworkUseCase(homework)
            cancelNotification(homework.id)
        }
    }

    private fun cancelNotification(homeworkId: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            homeworkId.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}