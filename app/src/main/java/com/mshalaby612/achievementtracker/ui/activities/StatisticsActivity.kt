package com.mshalaby612.achievementtracker.ui.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mshalaby612.achievementtracker.R
import com.mshalaby612.achievementtracker.data.SHARED_PREFERENCES_BEST_DAY
import com.mshalaby612.achievementtracker.data.SHARED_PREFERENCES_NAME
import com.mshalaby612.achievementtracker.data.database.ProgressDay
import com.mshalaby612.achievementtracker.data.database.ProgressDayDao
import com.mshalaby612.achievementtracker.data.database.ProgressDaysDatabase
import com.mshalaby612.achievementtracker.data.fromJsonProgressDay
import com.mshalaby612.achievementtracker.ui.adapters.MyLinearLayoutManager
import com.mshalaby612.achievementtracker.ui.adapters.StatisticsAdapter
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var database: ProgressDaysDatabase? = null
    private var databaseDao: ProgressDayDao? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        sharedPreferences =
            applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        setSupportActionBar(toolbar)
        title = "Statistics"

        val json = sharedPreferences.getString(SHARED_PREFERENCES_BEST_DAY, null)
        val bestDay =
            fromJsonProgressDay(json)

        tv_day_name.text = if (bestDay?.date.isNullOrEmpty()) {"No data"} else bestDay?.date ?: "No data"
        tv_day_progress.apply {
            val timeMinutes = bestDay?.ingredients?.sumBy { it.timeMinutes } ?: 0
            text = if (timeMinutes > 0)
                getString(R.string._100_minutes, timeMinutes)
            else
                "No data"
        }

        GlobalScope.launch {
            database = ProgressDaysDatabase.getAppDataBase(applicationContext)
            databaseDao = database?.getProgressDayDao()
            val days = databaseDao?.getLimited(7)
            if (days?.size ?: 0 < 7) {
                fillDays(days)
            }
            withContext(Dispatchers.Main) {
                val statisticsAdapter = StatisticsAdapter(days)
                val manager = MyLinearLayoutManager(this@StatisticsActivity)
                recycler_statistics.apply {
                    adapter = statisticsAdapter
                    layoutManager = manager
                }
            }
        }

    }

    private fun fillDays(days: MutableList<ProgressDay>?) {
        for (i in 0..6 - (days?.size ?: 0)) {
            days?.add(ProgressDay(0, "No data", arrayListOf()))
        }
    }
}