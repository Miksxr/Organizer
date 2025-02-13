package com.example.organizer.domain.usecase

import com.example.organizer.data.mapper.toDomain
import com.example.organizer.data.repository.SubjectRepositoryImpl
import com.example.organizer.domain.model.Subject
import com.example.organizer.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSubjectsUseCase @Inject constructor(
    private val repository: SubjectRepository
) {
    operator fun invoke(): Flow<List<Subject>> =
        repository.getAllSubjects()
}