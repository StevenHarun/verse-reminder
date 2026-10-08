package com.versereminder.app.data.repository

import com.versereminder.app.data.local.SeedReadingPlans
import com.versereminder.app.data.local.dao.ReadingPlanDao
import com.versereminder.app.data.local.entity.ReadingPlanProgressEntity
import com.versereminder.app.domain.model.ReadingPlan
import com.versereminder.app.domain.model.ReadingPlanProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReadingPlanRepository(
    private val readingPlanDao: ReadingPlanDao
) {

    fun getAllPlans(): List<ReadingPlan> = SeedReadingPlans.plans

    fun getPlanById(planId: String): ReadingPlan? = SeedReadingPlans.getPlanById(planId)

    fun getAllProgress(): Flow<Map<String, ReadingPlanProgress>> {
        return readingPlanDao.getAllProgress().map { entities ->
            entities.associate { entity -> entity.planId to entity.toDomain() }
        }
    }

    fun getProgress(planId: String): Flow<ReadingPlanProgress> {
        return readingPlanDao.getProgressByPlanId(planId).map { entity ->
            entity?.toDomain() ?: ReadingPlanProgress(planId = planId)
        }
    }

    suspend fun toggleDayCompletion(planId: String, dayNumber: Int) {
        val plan = getPlanById(planId) ?: return
        val currentEntity = readingPlanDao.getProgressByPlanIdSync(planId)
        val currentProgress = currentEntity?.toDomain() ?: ReadingPlanProgress(planId = planId)

        val updatedCompletedDays = currentProgress.completedDays.toMutableSet()
        if (updatedCompletedDays.contains(dayNumber)) {
            updatedCompletedDays.remove(dayNumber)
        } else {
            updatedCompletedDays.add(dayNumber)
        }

        val isFinished = updatedCompletedDays.size >= plan.totalDays
        val nextDay = if (dayNumber < plan.totalDays) dayNumber + 1 else dayNumber

        val updatedProgress = currentProgress.copy(
            completedDays = updatedCompletedDays,
            currentDay = nextDay,
            lastReadTimestamp = System.currentTimeMillis(),
            isCompleted = isFinished
        )

        readingPlanDao.saveProgress(ReadingPlanProgressEntity.fromDomain(updatedProgress))
    }

    suspend fun resetProgress(planId: String) {
        readingPlanDao.deleteProgress(planId)
    }
}
