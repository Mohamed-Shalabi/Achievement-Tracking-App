package com.mshalaby612.achievementtracker.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mshalaby612.achievementtracker.data.Ingredient

@Entity
data class ProgressDay(
    @PrimaryKey
    val idTimeMillis: Long,
    @ColumnInfo
    val date: String,
    @ColumnInfo
    val ingredients: MutableList<Ingredient>
)