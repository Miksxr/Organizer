package com.example.organizer.domain.usecase

import com.example.organizer.data.local.entity.HomeworkEntity
import com.example.organizer.domain.repository.HomeworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeworksUseCase @Inject constructor(
    private val repository: HomeworkRepository
) {
    operator fun invoke(subjectId: Int): Flow<List<HomeworkEntity>> =
        repository.getHomeworksForSubject(subjectId)
}