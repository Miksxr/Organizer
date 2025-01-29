package com.example.organizer.data.repository

import com.example.organizer.data.local.dao.SubjectDao
import com.example.organizer.data.local.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepository @Inject constructor(
    private val dao: SubjectDao
) {
    fun getAllSubjects(): Flow<List<SubjectEntity>> = dao.getAllSubjects()

    suspend fun addSubject(subject: SubjectEntity) = dao.insert(subject)

    suspend fun updateSubject(subject: SubjectEntity) = dao.update(subject)

    suspend fun deleteSubject(subject: SubjectEntity) = dao.delete(subject)
}