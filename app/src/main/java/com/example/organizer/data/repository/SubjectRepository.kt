package com.example.organizer.data.repository

import com.example.organizer.data.local.dao.SubjectDao
import com.example.organizer.data.local.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepository @Inject constructor(
    private val subjectDao: SubjectDao
) {
    fun getAllSubjects(): Flow<List<SubjectEntity>> = subjectDao.getAllSubjects()

    suspend fun addSubject(subject: SubjectEntity) {
        subjectDao.insert(subject)
    }
}