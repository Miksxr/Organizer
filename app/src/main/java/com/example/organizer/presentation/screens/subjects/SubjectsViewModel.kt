package com.example.organizer.presentation.screens.subjects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizer.data.local.entity.SubjectEntity
import com.example.organizer.data.mapper.toDomain
import com.example.organizer.domain.usecase.AddSubjectUseCase
import com.example.organizer.domain.usecase.DeleteSubjectUseCase
import com.example.organizer.domain.usecase.GetSubjectsUseCase
import com.example.organizer.domain.usecase.UpdateSubjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val getSubjectsUseCase: GetSubjectsUseCase,
    private val addSubjectUseCase: AddSubjectUseCase,
    private val updateSubjectUseCase: UpdateSubjectUseCase,
    private val deleteSubjectUseCase: DeleteSubjectUseCase
) : ViewModel() {

    val subjects = getSubjectsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addSubject(name: String, teacherName: String, photoUri: String?) {
        viewModelScope.launch {
            val subjectEntity = SubjectEntity(
                name = name,
                teacherName = teacherName,
                photoUri = photoUri
            )
            addSubjectUseCase(subjectEntity.toDomain())
        }
    }

    fun updateSubject(subjectId: Long, newName: String, newTeacherName: String, photoUri: String?) {
        viewModelScope.launch {
            val subjectEntity = SubjectEntity(
                id = subjectId,
                name = newName,
                teacherName = newTeacherName,
                photoUri = photoUri
            )
            updateSubjectUseCase(subjectEntity.toDomain())
        }
    }

    fun deleteSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            deleteSubjectUseCase(subject.toDomain())
        }
    }
}