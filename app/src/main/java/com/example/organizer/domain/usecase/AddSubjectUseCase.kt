package com.example.organizer.domain.usecase

import com.example.organizer.data.local.entity.SubjectEntity
import com.example.organizer.domain.model.Subject
import com.example.organizer.domain.repository.SubjectRepository
import javax.inject.Inject

class AddSubjectUseCase @Inject constructor(
    private val repository: SubjectRepository
) {
    suspend operator fun invoke(subject: Subject) = repository.addSubject(subject)
}