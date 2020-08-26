package com.mshalaby612.achievementtracker.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mshalaby612.achievementtracker.data.database.ProgressDay
import java.lang.reflect.Type


const val SHARED_PREFERENCES_NAME = "dailyProgress"

const val SHARED_PREFERENCES_TODAY_PROGRESS = "todayProgress"

const val SHARED_PREFERENCES_THREE_STARS = "threeStars"

const val SHARED_PREFERENCES_THREE_STARS_DEFAULT_VALUE = 240

const val SHARED_PREFERENCES_AUTO_START = "autoStartSession"

const val SHARED_PREFERENCES_BEST_DAY = "bestDay"

//const val SHARED_PREFERENCES_TODAY_ENTITY = "todayEntity"

fun List<Ingredient>.toJsonIngredients(): String {
    val type: Type = object : TypeToken<List<Ingredient?>?>() { }.type
    return Gson().toJson(this, type)
}

fun listFromJsonIngredients(json: String?): List<Ingredient> {
    if (json.isNullOrEmpty()) {
        return listOf()
    }
    val type: Type = object : TypeToken<List<Ingredient?>?>() { }.type
    return Gson().fromJson<List<Ingredient>>(json, type) ?: listOf()
}

fun ProgressDay.toJsonProgressDay(): String {
    val type: Type = object : TypeToken<ProgressDay?>() { }.type
    return Gson().toJson(this, type)
}

fun fromJsonProgressDay(json: String?): ProgressDay? {
    if (json.isNullOrEmpty()) {
        return ProgressDay(0, "", mutableListOf())
    }
    val type: Type = object : TypeToken<ProgressDay?>() { }.type
    return Gson().fromJson<ProgressDay>(json, type)
}