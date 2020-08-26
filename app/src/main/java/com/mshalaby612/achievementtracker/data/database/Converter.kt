package com.mshalaby612.achievementtracker.data.database

import androidx.room.TypeConverter
import com.mshalaby612.achievementtracker.data.Ingredient
import com.mshalaby612.achievementtracker.data.listFromJsonIngredients
import com.mshalaby612.achievementtracker.data.toJsonIngredients

class Converter {

    @TypeConverter
    fun fromList(list: List<Ingredient>): String {
        return list.toJsonIngredients()
    }

    @TypeConverter
    fun toList(string: String): List<Ingredient> {
        return listFromJsonIngredients(string)
    }
}