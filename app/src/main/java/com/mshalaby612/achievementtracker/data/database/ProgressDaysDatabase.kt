package com.mshalaby612.achievementtracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ProgressDay::class], version = 1)
@TypeConverters(Converter::class)
abstract class ProgressDaysDatabase : RoomDatabase() {

    abstract fun getProgressDayDao(): ProgressDayDao

    companion object {
        private var INSTANCE: ProgressDaysDatabase? = null

        fun getAppDataBase(context: Context): ProgressDaysDatabase? {
            if (INSTANCE == null) {
                synchronized(ProgressDaysDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ProgressDaysDatabase::class.java,
                        "ProgressDaysDatabase"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }

}