package com.example.organizer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "homeworks")
data class HomeworkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: Int,
    val description: String,
    val dueDate: String,
    val isCompleted: Boolean = false // Новое поле
)