package com.mshalaby612.achievementtracker

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.mshalaby612.achievementtracker.recievers.DailyProgressReceiver
import java.util.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val intent = Intent(this, DailyProgressReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, Intent.FILL_IN_ACTION)
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            (this.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            (this.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}