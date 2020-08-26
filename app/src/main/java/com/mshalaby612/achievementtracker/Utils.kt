package com.mshalaby612.achievementtracker

import java.util.*

fun calculateRemainingOfTheDayMillis(): Long {
    return calculateTodayTime() + 24 * 60 * 60 * 1000 - Calendar.getInstance().timeInMillis

}

fun calculateTodayTime(): Long {
    return Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}