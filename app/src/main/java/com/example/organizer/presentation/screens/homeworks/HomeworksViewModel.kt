package com.example.organizer.presentation.screens.homeworks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizer.data.local.entity.HomeworkEntity
import com.example.organizer.domain.usecase.AddHomeworkUseCase
import com.example.organizer.domain.usecase.DeleteHomeworkUseCase
import com.example.organizer.domain.usecase.GetHomeworksUseCase
import com.example.organizer.domain.usecase.UpdateHomeworkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeworksViewModel @Inject constructor(
    private val getHomeworksUseCase: GetHomeworksUseCase,
    private val addHomeworkUseCase: AddHomeworkUseCase,
    private val updateHomeworkUseCase: UpdateHomeworkUseCase,
    private val deleteHomeworkUseCase: DeleteHomeworkUseCase
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
                subjectId = subjectId,
                title = title,
                description = description,
                dueDate = dueDate
            )
            addHomeworkUseCase(homework)
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
        }
    }
}