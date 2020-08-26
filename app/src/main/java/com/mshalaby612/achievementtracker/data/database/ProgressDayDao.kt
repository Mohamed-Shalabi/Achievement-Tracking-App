package com.mshalaby612.achievementtracker.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProgressDayDao {
    @Insert
    suspend fun insert(progressDay: ProgressDay)

    @Update
    suspend fun update(progressDay: ProgressDay)

    @Query("SELECT * FROM ProgressDay")
    suspend fun getAll(): List<ProgressDay>

    @Query("SELECT * FROM ProgressDay WHERE idTimeMillis = :id")
    suspend fun get(id: Long): List<ProgressDay>

    @Query("SELECT * FROM ProgressDay LIMIT :limit")
    suspend fun getLimited(limit: Int): MutableList<ProgressDay>

    @Insert
    suspend  fun insertAll(vararg progressDays: ProgressDay)

}