package com.example.organizer.presentation.screens.grades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizer.data.local.entity.GradeEntity
import com.example.organizer.domain.usecase.AddGradeUseCase
import com.example.organizer.domain.usecase.DeleteGradeUseCase
import com.example.organizer.domain.usecase.GetGradesUseCase
import com.example.organizer.domain.usecase.UpdateGradeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradesViewModel @Inject constructor(
    private val getGradesUseCase: GetGradesUseCase,
    private val addGradeUseCase: AddGradeUseCase,
    private val updateGradeUseCase: UpdateGradeUseCase,
    private val deleteGradeUseCase: DeleteGradeUseCase
) : ViewModel() {

    private val _grades = MutableStateFlow<List<GradeEntity>>(emptyList())
    val grades: StateFlow<List<GradeEntity>> = _grades

    fun loadGradesForSubject(subjectId: Int) {
        viewModelScope.launch {
            getGradesUseCase(subjectId).collect {
                _grades.value = it
            }
        }
    }

    fun addGrade(workType: String, gradeValue: Int, date: String, subjectId: Int) {
        viewModelScope.launch {
            val grade = GradeEntity(
                subjectId = subjectId,
                workType = workType,
                grade = gradeValue,
                date = date
            )
            addGradeUseCase(grade)
        }
    }

    fun updateGrade(grade: GradeEntity) {
        viewModelScope.launch {
            updateGradeUseCase(grade)
        }
    }

    fun deleteGrade(grade: GradeEntity) {
        viewModelScope.launch {
            deleteGradeUseCase(grade)
        }
    }
}