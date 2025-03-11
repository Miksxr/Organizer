package com.example.organizer.domain.usecase

import com.example.organizer.data.local.entity.HomeworkEntity
import com.example.organizer.domain.repository.HomeworkRepository
import javax.inject.Inject

class DeleteHomeworkUseCase @Inject constructor(
    private val repository: HomeworkRepository
) {
    suspend operator fun invoke(homework: HomeworkEntity) = repository.deleteHomework(homework)
}
