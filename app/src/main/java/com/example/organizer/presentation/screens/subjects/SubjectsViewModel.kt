package com.example.organizer.presentation.screens.subjects

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizer.data.local.entity.SubjectEntity
import com.example.organizer.data.mapper.toDomain
import com.example.organizer.domain.usecase.AddSubjectUseCase
import com.example.organizer.domain.usecase.DeleteSubjectUseCase
import com.example.organizer.domain.usecase.GetSubjectsUseCase
import com.example.organizer.domain.usecase.SaveImageUseCase
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
    private val deleteSubjectUseCase: DeleteSubjectUseCase,
    private val saveImageUseCase: SaveImageUseCase,
) : ViewModel() {

    val subjects = getSubjectsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addSubject(name: String, teacherName: String, photoPath: String?) {
        viewModelScope.launch {
            val subjectEntity = SubjectEntity(
                name = name,
                teacherName = teacherName,
                photoPath = photoPath
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
                photoPath = photoUri
            )
            updateSubjectUseCase(subjectEntity.toDomain())
        }
    }

    fun deleteSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            deleteSubjectUseCase(subject.toDomain())
        }
    }

    suspend fun saveImage(uri: Uri): String? {
        return saveImageUseCase(uri)
    }
}