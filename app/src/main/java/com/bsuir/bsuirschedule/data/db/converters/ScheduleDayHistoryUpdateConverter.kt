package com.bsuir.bsuirschedule.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.bsuir.bsuirschedule.domain.models.ScheduleDayUpdateHistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class ScheduleDayHistoryUpdateConverter {

    @TypeConverter
    fun fromScheduleToString(schedule: ArrayList<ScheduleDayUpdateHistory>?): String? {
        return Gson().toJson(schedule)
    }

    @TypeConverter
    fun fromStringToSchedule(schedule: String?): ArrayList<ScheduleDayUpdateHistory>? {
        val listType = object: TypeToken<ArrayList<ScheduleDayUpdateHistory>>(){}.type
        return Gson().fromJson(schedule, listType)
    }

}


