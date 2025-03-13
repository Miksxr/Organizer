package com.example.organizer.data.repositoryimpl

import com.example.organizer.data.local.dao.GradeDao
import com.example.organizer.data.local.entity.GradeEntity
import com.example.organizer.domain.repository.GradeRepository
import javax.inject.Inject

class GradeRepositoryImpl @Inject constructor(
    private val dao: GradeDao
) : GradeRepository {
    override fun getGradesForSubject(subjectId: Int) = dao.getGradesForSubject(subjectId)
    override suspend fun addGrade(grade: GradeEntity) = dao.insert(grade)
    override suspend fun updateGrade(grade: GradeEntity) = dao.update(grade)
    override suspend fun deleteGrade(grade: GradeEntity) = dao.delete(grade)
}