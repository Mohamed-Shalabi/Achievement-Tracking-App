package com.mshalaby612.achievementtracker.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshalaby612.achievementtracker.R
import com.mshalaby612.achievementtracker.data.database.ProgressDay
import com.mshalaby612.achievementtracker.data.database.ProgressDaysDatabase
import com.mshalaby612.achievementtracker.ui.adapters.TrackingAdapter
import kotlinx.android.synthetic.main.activity_tracking.*
import kotlinx.coroutines.*

class TrackingActivity : AppCompatActivity() {

    private var database: ProgressDaysDatabase? = null
    private var daysOfProgress = mutableListOf<ProgressDay>()

    @ObsoleteCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        setSupportActionBar(toolbar)
        title = "Daily Progress"

        GlobalScope.launch {
            database = ProgressDaysDatabase.getAppDataBase(applicationContext)
            daysOfProgress =
                (database?.getProgressDayDao()?.getAll() as MutableList<ProgressDay>?)
                    ?: mutableListOf()

            if (daysOfProgress.size > 0) {

                withContext(Dispatchers.Main) {
                    tv_no_data.visibility = View.GONE
                    recycler_tracking.setHasFixedSize(true)
                    val adapter = TrackingAdapter(daysOfProgress) { }
                    recycler_tracking.apply {
                        layoutManager = LinearLayoutManager(this@TrackingActivity)
                        this.adapter = adapter
                    }

                }
            } else {
                tv_no_data.visibility = View.VISIBLE
            }
        }

    }

}