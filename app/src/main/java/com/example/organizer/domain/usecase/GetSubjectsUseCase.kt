package com.example.organizer.domain.usecase

import com.example.organizer.data.local.entity.SubjectEntity
import com.example.organizer.data.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.security.auth.Subject

class GetSubjectsUseCase @Inject constructor(
    private val repository: SubjectRepository
) {
    operator fun invoke(): Flow<List<SubjectEntity>> = repository.getAllSubjects()
}