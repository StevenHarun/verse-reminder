package com.versereminder.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.versereminder.app.data.local.entity.ReadingPlanProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingPlanDao {

    @Query("SELECT * FROM reading_plan_progress")
    fun getAllProgress(): Flow<List<ReadingPlanProgressEntity>>

    @Query("SELECT * FROM reading_plan_progress WHERE planId = :planId LIMIT 1")
    fun getProgressByPlanId(planId: String): Flow<ReadingPlanProgressEntity?>

    @Query("SELECT * FROM reading_plan_progress WHERE planId = :planId LIMIT 1")
    suspend fun getProgressByPlanIdSync(planId: String): ReadingPlanProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: ReadingPlanProgressEntity)

    @Query("DELETE FROM reading_plan_progress WHERE planId = :planId")
    suspend fun deleteProgress(planId: String)
}
