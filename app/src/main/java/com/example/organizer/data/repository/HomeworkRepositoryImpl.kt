package com.example.organizer.data.repository

import com.example.organizer.data.local.dao.HomeworkDao
import com.example.organizer.data.local.entity.HomeworkEntity
import com.example.organizer.domain.repository.HomeworkRepository
import javax.inject.Inject

class HomeworkRepositoryImpl @Inject constructor(
    private val dao: HomeworkDao
) : HomeworkRepository {
    override fun getHomeworksForSubject(subjectId: Int) = dao.getHomeworksForSubject(subjectId)
    override suspend fun addHomework(homework: HomeworkEntity) = dao.insert(homework)
    override suspend fun updateHomework(homework: HomeworkEntity) = dao.update(homework)
    override suspend fun deleteHomework(homework: HomeworkEntity) = dao.delete(homework)
}