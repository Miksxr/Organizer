package com.example.organizer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.organizer.data.local.dao.GradeDao
import com.example.organizer.data.local.dao.HomeworkDao
import com.example.organizer.data.local.dao.SubjectDao
import com.example.organizer.data.local.entity.GradeEntity
import com.example.organizer.data.local.entity.HomeworkEntity
import com.example.organizer.data.local.entity.SubjectEntity

@Database(entities = [SubjectEntity::class, GradeEntity::class, HomeworkEntity::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun gradeDao(): GradeDao
    abstract fun homeworkDao(): HomeworkDao
}