package com.example.organizer.domain.usecase

import com.example.organizer.data.local.entity.SubjectEntity
import com.example.organizer.data.repository.SubjectRepository
import javax.inject.Inject

class DeleteSubjectUseCase @Inject constructor(
    private val repository: SubjectRepository
) {
    suspend operator fun invoke(subject: SubjectEntity) {
        repository.deleteSubject(subject)
    }
}