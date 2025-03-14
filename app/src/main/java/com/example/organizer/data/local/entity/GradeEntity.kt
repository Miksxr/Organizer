package com.example.organizer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grades")
data class GradeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: Int,
    val workType: String,
    val grade: Int,
    val date: String
)