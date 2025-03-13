package com.example.organizer.domain.model

data class Subject(
    val id: Long,
    val name: String,
    val teacherName: String,
    val photoPath: String? = null // Новое поле
)