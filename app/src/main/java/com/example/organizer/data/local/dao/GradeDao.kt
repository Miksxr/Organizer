package com.example.organizer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.organizer.data.local.entity.GradeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GradeDao {
    @Insert
    suspend fun insert(grade: GradeEntity)

    @Query("SELECT * FROM grades WHERE subjectId = :subjectId")
    fun getGradesForSubject(subjectId: Int): Flow<List<GradeEntity>>
}