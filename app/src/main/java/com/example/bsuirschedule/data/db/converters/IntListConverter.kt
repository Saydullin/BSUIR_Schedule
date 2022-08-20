package com.example.bsuirschedule.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class IntListConverter {

    @TypeConverter
    fun fromIntListToString(intList: List<Int>?): String? {
        return Gson().toJson(intList)
    }

    @TypeConverter
    fun fromStringToIntList(intListJSON: String): List<Int>? {
        val listType = object: TypeToken<List<Int>>(){}.type
        return Gson().fromJson(intListJSON, listType)
    }

}