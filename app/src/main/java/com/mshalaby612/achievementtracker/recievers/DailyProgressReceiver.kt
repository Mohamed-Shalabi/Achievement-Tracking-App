package com.mshalaby612.achievementtracker.recievers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.mshalaby612.achievementtracker.calculateTodayTime
import com.mshalaby612.achievementtracker.data.SHARED_PREFERENCES_NAME
import com.mshalaby612.achievementtracker.data.SHARED_PREFERENCES_TODAY_PROGRESS

class DailyProgressReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val sharedPreferences =
            context?.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE) ?: return

//        val lastDayIngredients = listFromJson(
//            sharedPreferences.getString(
//                SHARED_PREFERENCES_TODAY_ENTITY,
//                null
//            )
//        )
//
//        GlobalScope.launch {
//            val database: ProgressDaysDatabase? = ProgressDaysDatabase.getAppDataBase(context)
//            database?.getProgressDayDao()?.insert(
//                ProgressDay(
//                    idTimeMillis = Calendar.getInstance().timeInMillis,
//                    date = Calendar.getInstance().time.toString(),
//                    ingredients = lastDayIngredients
//                )
//            )
//        }

        with(sharedPreferences.edit()) {
            putInt(SHARED_PREFERENCES_TODAY_PROGRESS, 0)
            //putString(SHARED_PREFERENCES_TODAY_ENTITY, listOf<Ingredient>().toJson())
            apply()
        }
        println("Triggered!")
        Toast.makeText(context, "Triggered!", Toast.LENGTH_LONG).show()
        val intent2 = Intent(context, DailyProgressReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calculateTodayTime() + 24 * 60 * 60 * 1000,
                pendingIntent
            )
        } else {
            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExact(
                AlarmManager.RTC_WAKEUP,
                calculateTodayTime() + 24 * 60 * 60 * 1000,
                pendingIntent
            )
        }

    }

}