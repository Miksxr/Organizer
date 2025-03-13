package com.example.organizer.data.mapper

import com.example.organizer.data.local.entity.SubjectEntity
import com.example.organizer.domain.model.Subject

fun Subject.toEntity(): SubjectEntity {
    return SubjectEntity(
        id = this.id,
        name = this.name,
        teacherName = this.teacherName,
        photoPath = this.photoPath
    )
}

fun SubjectEntity.toDomain(): Subject {
    return Subject(
        id = this.id,
        name = this.name,
        teacherName = this.teacherName,
        photoPath = this.photoPath
    )
}

