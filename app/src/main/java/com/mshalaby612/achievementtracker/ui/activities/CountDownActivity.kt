package com.mshalaby612.achievementtracker.ui.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mshalaby612.achievementtracker.R
import com.mshalaby612.achievementtracker.calculateTodayTime
import com.mshalaby612.achievementtracker.data.*
import com.mshalaby612.achievementtracker.data.database.ProgressDay
import com.mshalaby612.achievementtracker.data.database.ProgressDayDao
import com.mshalaby612.achievementtracker.data.database.ProgressDaysDatabase
import kotlinx.android.synthetic.main.activity_count_down.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*


class CountDownActivity : AppCompatActivity() {

    private var remainingTime: Long = 0
    private lateinit var countDownTimer: CountDownTimer
    private var running = true
    var period: Int = 0
    private lateinit var sharedPreferences: SharedPreferences
    private var database: ProgressDaysDatabase? = null
    private var databaseDao: ProgressDayDao? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)

        sharedPreferences =
            applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        val start = sharedPreferences.getBoolean(SHARED_PREFERENCES_AUTO_START, true)

        period = intent.getIntExtra("period", 0)

        remainingTime = savedInstanceState?.getLong("remaining", period * 60L * 1000)
            ?: period.toLong() * 60 * 1000

        if (period == 0) {
            finish()
            overridePendingTransition(R.anim.nothing, android.R.anim.fade_out)
        }

        countDownTimer = makeCountDownTimer(remainingTime)
        if (start) {
            countDownTimer.start()
            running = true
        } else {
            start_pause_button.performClick()
            time_tv.text = String.format(
                "%02d:%02d",
                remainingTime / 1000 / 60,
                remainingTime / 1000 % 60
            )
        }

        stop_button.setOnClickListener {
            showDialog {
                finish()
            }
        }
        GlobalScope.launch {
            database = ProgressDaysDatabase.getAppDataBase(applicationContext)
            databaseDao = database?.getProgressDayDao()
        }
    }

    private fun makeCountDownTimer(remainingTime: Long): CountDownTimer {
        return object : CountDownTimer(remainingTime, 1000) {
            override fun onFinish() {
                time_tv.text = "00:00"
                this@CountDownActivity.onFinish(period)
            }

            override fun onTick(millisUntilFinished: Long) {
                this@CountDownActivity.remainingTime = millisUntilFinished
                time_tv.text = String.format(
                    "%02d:%02d",
                    millisUntilFinished / 1000 / 60,
                    millisUntilFinished / 1000 % 60
                )
            }

        }
    }

    private fun showDialog(onYesPressed: () -> Unit) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("The progress will not be saved, continue?")
        builder.setPositiveButton("Yes") { _, which ->
            if (which == AlertDialog.BUTTON_POSITIVE) {
                Toast.makeText(this@CountDownActivity, "Unsaved", Toast.LENGTH_LONG).show()
                onYesPressed.invoke()
                overridePendingTransition(R.anim.nothing, android.R.anim.fade_out)
            }
        }
        builder.setNegativeButton("No") { _, which ->
            if (which == AlertDialog.BUTTON_NEGATIVE) {
                Toast.makeText(this@CountDownActivity, "Continue", Toast.LENGTH_LONG).show()
            }
        }
        builder.create().show()
    }

    private fun onFinish(period: Int) {
        val num = sharedPreferences.getInt(SHARED_PREFERENCES_TODAY_PROGRESS, 0)
        val todayProgress = num + period
        val editor = sharedPreferences.edit()
        with(editor) {
            putInt(SHARED_PREFERENCES_TODAY_PROGRESS, todayProgress)
            apply()
        }

        GlobalScope.launch {

            updateDatabaseAsync(period).await()

            val bestDay =
                fromJsonProgressDay(sharedPreferences.getString(SHARED_PREFERENCES_BEST_DAY, null))

            val bestDayProgress = bestDay?.ingredients?.sumOf { it.timeMinutes } ?: 0

            if (todayProgress > bestDayProgress) {
                with(editor) {
                    putString(
                        SHARED_PREFERENCES_BEST_DAY,
                        databaseDao?.get(calculateTodayTime())?.get(0)?.toJsonProgressDay()
                    )
                    apply()
                }
            }
        }
//        try {
//            MainActivity.todayIngredients?.add(MainActivity.ingredients.find { it.timeMinutes == period }!!)
//            with(sharedPreferences.edit()) {
//                putString(SHARED_PREFERENCES_TODAY_ENTITY, (MainActivity.ingredients as List<Ingredient>).toJson())
//                apply()
//            }
//        } catch (ignored: Exception) {
//        }

        finish()
        overridePendingTransition(R.anim.nothing, android.R.anim.fade_out)
    }

    private fun updateDatabaseAsync(period: Int) = GlobalScope.async {
        if (databaseDao?.get(calculateTodayTime()) == null || databaseDao?.get(
                calculateTodayTime()
            )!!.isEmpty()
        ) {

            databaseDao?.insert(

                ProgressDay(

                    calculateTodayTime(),

                    buildString {
                        val tokenizer = StringTokenizer(Date().toString(), " ")
                        append(tokenizer.nextToken())
                        append(" ")
                        append(tokenizer.nextToken())
                        append(" ")
                        append(tokenizer.nextToken())
                    },

                    mutableListOf(MainActivity.ingredients.first {
                        it.timeMinutes == period
                    })
                )
            )
        } else {

            val progressDay = databaseDao?.get(calculateTodayTime())

            databaseDao?.update(

                ProgressDay(

                    calculateTodayTime(),

                    buildString {
                        val tokenizer = StringTokenizer(Date().toString(), " ")
                        append(tokenizer.nextToken())
                        append(" ")
                        append(tokenizer.nextToken())
                        append(" ")
                        append(tokenizer.nextToken())
                    },

                    progressDay?.get(0)?.ingredients?.apply {
                        add(MainActivity.ingredients.first {
                            it.timeMinutes == period
                        })
                    }
                        ?: mutableListOf(MainActivity.ingredients.first {
                            it.timeMinutes == period
                        })
                )
            )
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putLong("remaining", remainingTime)
        }

    }

    fun pauseStart(view: View) {
        if (running) {
            start_pause_button.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            pause(remainingTime)
        } else {
            start_pause_button.setImageResource(R.drawable.ic_baseline_pause_24)
            resume()
        }
        running = running.not()
    }

    fun pause(period: Long) {
        val countDownTimerTemp = makeCountDownTimer(period)
        countDownTimer.cancel()
        countDownTimer = countDownTimerTemp
    }

    fun resume() {
        countDownTimer.start()
    }

    override fun onBackPressed() {
        showDialog {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            countDownTimer.cancel()
        } catch (e: Exception) {

        }
    }
}
