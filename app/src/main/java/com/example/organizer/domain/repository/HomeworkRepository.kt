package com.example.organizer.domain.repository

import com.example.organizer.data.local.dao.HomeworkDao
import com.example.organizer.data.local.entity.HomeworkEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface HomeworkRepository {
    fun getHomeworksForSubject(subjectId: Int): Flow<List<HomeworkEntity>>
    suspend fun addHomework(homework: HomeworkEntity)
    suspend fun updateHomework(homework: HomeworkEntity)
    suspend fun deleteHomework(homework: HomeworkEntity)
}
