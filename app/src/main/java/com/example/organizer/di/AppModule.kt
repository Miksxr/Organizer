package com.example.organizer.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.organizer.data.local.AppDatabase
import com.example.organizer.data.local.dao.GradeDao
import com.example.organizer.data.local.dao.HomeworkDao
import com.example.organizer.data.local.dao.SubjectDao
import com.example.organizer.data.repositoryimpl.FileRepositoryImpl
import com.example.organizer.data.repositoryimpl.GradeRepositoryImpl
import com.example.organizer.data.repositoryimpl.HomeworkRepositoryImpl
import com.example.organizer.data.repositoryimpl.SubjectRepositoryImpl
import com.example.organizer.domain.repository.FileRepository
import com.example.organizer.domain.repository.GradeRepository
import com.example.organizer.domain.repository.HomeworkRepository
import com.example.organizer.domain.repository.SubjectRepository
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
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideSubjectDao(database: AppDatabase): SubjectDao = database.subjectDao()

    @Provides
    fun provideSubjectRepository(subjectDao: SubjectDao): SubjectRepository {
        return SubjectRepositoryImpl(subjectDao)
    }

    @Provides
    fun provideHomeworkDao(database: AppDatabase): HomeworkDao = database.homeworkDao()

    @Provides
    fun provideHomeworkRepository(homeworkDao: HomeworkDao): HomeworkRepository {
        return HomeworkRepositoryImpl(homeworkDao)
    }

    @Provides
    fun provideGradeDao(database: AppDatabase): GradeDao = database.gradeDao()

    @Provides
    fun provideGradeRepository(gradeDao: GradeDao): GradeRepository {
        return GradeRepositoryImpl(gradeDao)
    }

    @Provides
    @Singleton
    fun provideFileRepository(
        @ApplicationContext context: Context
    ): FileRepository {
        return FileRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("settings") }
        )
    }
}