package com.mshalaby612.achievementtracker.data

import com.mshalaby612.achievementtracker.R

data class Ingredient(
    val timeMinutes: Int,
    val image: Int = R.mipmap.ic_launcher_round,
    val usageNum: Int = 0
) {
    val timeMillis
        get() = (timeMinutes * 60 * 1000).toLong()
    val id = timeMinutes

}