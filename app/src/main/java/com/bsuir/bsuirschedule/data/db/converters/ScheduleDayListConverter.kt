package com.bsuir.bsuirschedule.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class ScheduleDayListConverter {

    @TypeConverter
    fun fromScheduleToString(schedule: ArrayList<ScheduleDay>?): String? {
        return Gson().toJson(schedule)
    }

    @TypeConverter
    fun fromStringToSchedule(schedule: String): ArrayList<ScheduleDay>? {
        val listType = object: TypeToken<ArrayList<ScheduleDay>>(){}.type
        return Gson().fromJson(schedule, listType)
    }

}


