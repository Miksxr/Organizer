package com.example.organizer.domain.repository

import com.example.organizer.data.local.entity.GradeEntity
import kotlinx.coroutines.flow.Flow

interface GradeRepository {
    fun getGradesForSubject(subjectId: Int): Flow<List<GradeEntity>>
    suspend fun addGrade(grade: GradeEntity)
    suspend fun updateGrade(grade: GradeEntity)
    suspend fun deleteGrade(grade: GradeEntity)
}