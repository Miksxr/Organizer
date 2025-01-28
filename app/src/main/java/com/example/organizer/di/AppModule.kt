package com.example.organizer.di

import android.content.Context
import androidx.room.Room
import com.example.organizer.data.local.AppDatabase
import com.example.organizer.data.local.dao.SubjectDao
import com.example.organizer.data.repository.SubjectRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "study_organizer.db"
        ).build()
    }

    @Provides
    fun provideSubjectDao(database: AppDatabase): SubjectDao = database.subjectDao()

    @Provides
    fun provideSubjectRepository(subjectDao: SubjectDao): SubjectRepository = SubjectRepository(subjectDao)
}