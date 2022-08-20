package com.example.bsuirschedule.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class StrListConverter {

    @TypeConverter
    fun fromListToString(departmentsList: List<String>?): String? {
        return Gson().toJson(departmentsList)
    }

    @TypeConverter
    fun fromStringToList(departmentsJSON: String): List<String>? {
        val listType = object: TypeToken<List<String>>(){}.type
        return Gson().fromJson(departmentsJSON, listType)
    }

}