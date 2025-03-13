package com.example.organizer.domain.usecase

import com.example.organizer.data.local.entity.GradeEntity
import com.example.organizer.domain.repository.GradeRepository
import javax.inject.Inject

class UpdateGradeUseCase @Inject constructor(
    private val repository: GradeRepository
) {
    suspend operator fun invoke(grade: GradeEntity) = repository.updateGrade(grade)
}