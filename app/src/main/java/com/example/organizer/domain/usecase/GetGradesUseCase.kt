package com.example.organizer.domain.usecase

import com.example.organizer.data.local.entity.GradeEntity
import com.example.organizer.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGradesUseCase @Inject constructor(
    private val repository: GradeRepository
) {
    operator fun invoke(subjectId: Int): Flow<List<GradeEntity>> =
        repository.getGradesForSubject(subjectId)
}