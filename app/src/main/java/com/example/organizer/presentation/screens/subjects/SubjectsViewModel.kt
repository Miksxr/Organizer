package com.example.organizer.presentation.screens.subjects

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizer.data.local.entity.SubjectEntity
import com.example.organizer.domain.usecase.AddSubjectUseCase
import com.example.organizer.domain.usecase.GetSubjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.security.auth.Subject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val getSubjectsUseCase: GetSubjectsUseCase,
    private val addSubjectUseCase: AddSubjectUseCase
) : ViewModel() {

    val subjects = getSubjectsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addSubject(name: String, teacherName: String) {
        viewModelScope.launch {
            addSubjectUseCase(SubjectEntity(name = name, teacherName = teacherName))
        }
    }
}