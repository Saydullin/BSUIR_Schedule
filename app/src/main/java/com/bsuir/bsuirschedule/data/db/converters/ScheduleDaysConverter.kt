package com.bsuir.bsuirschedule.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.bsuir.bsuirschedule.data.db.entities.GroupScheduleWeekTable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class ScheduleDaysConverter {

    @TypeConverter
    fun fromScheduleToString(scheduleDays: GroupScheduleWeekTable?): String? {
        return Gson().toJson(scheduleDays)
    }

    @TypeConverter
    fun fromStringToSchedule(scheduleDaysJSON: String?): GroupScheduleWeekTable? {
        val listType = object: TypeToken<GroupScheduleWeekTable>(){}.type
        return Gson().fromJson(scheduleDaysJSON, listType)
    }

}