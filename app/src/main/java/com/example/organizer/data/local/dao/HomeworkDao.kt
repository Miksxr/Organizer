package com.example.organizer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.organizer.data.local.entity.HomeworkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeworkDao {
    @Insert
    suspend fun insert(homework: HomeworkEntity)

    @Update
    suspend fun update(homework: HomeworkEntity)

    @Delete
    suspend fun delete(homework: HomeworkEntity)

    @Query("SELECT * FROM homeworks WHERE subjectId = :subjectId")
    fun getHomeworksForSubject(subjectId: Int): Flow<List<HomeworkEntity>>
}