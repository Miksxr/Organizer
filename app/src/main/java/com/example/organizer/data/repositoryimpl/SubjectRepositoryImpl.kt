package com.example.organizer.data.repositoryimpl

import com.example.organizer.data.local.dao.SubjectDao
import com.example.organizer.data.mapper.toDomain
import com.example.organizer.data.mapper.toEntity
import com.example.organizer.domain.model.Subject
import com.example.organizer.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SubjectRepositoryImpl @Inject constructor(
    private val dao: SubjectDao
) : SubjectRepository {

    override fun getAllSubjects(): Flow<List<Subject>> =
        dao.getAllSubjects().map { list -> list.map { it.toDomain() } }

    override suspend fun addSubject(subject: Subject) {
        dao.insert(subject.toEntity())
    }

    override suspend fun updateSubject(subject: Subject) {
        dao.update(subject.toEntity())
    }

    override suspend fun deleteSubject(subject: Subject) {
        dao.delete(subject.toEntity())
    }
}