package com.example.organizer.domain.usecase

import com.example.organizer.data.local.entity.SubjectEntity
import com.example.organizer.data.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSubjectsUseCase @Inject constructor(
    private val repository: SubjectRepository
) {
    operator fun invoke(): Flow<List<Subject>> =
        repository.getAllSubjects().map { list -> list.map { it.toDomain() } }
}

// Domain model
data class Subject(
    val id: Long,
    val name: String,
    val teacherName: String
)

// Мапперы
fun SubjectEntity.toDomain() = Subject(id, name, teacherName)
fun Subject.toEntity() = SubjectEntity(id, name, teacherName)