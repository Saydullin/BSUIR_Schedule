package com.bsuir.bsuirschedule.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.bsuir.bsuirschedule.data.db.entities.ScheduleSettingsTable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class ScheduleSettingsConverter {

    @TypeConverter
    fun fromScheduleToString(scheduleSettings: ScheduleSettingsTable?): String? {
        return Gson().toJson(scheduleSettings)
    }

    @TypeConverter
    fun fromStringToSchedule(scheduleSettings: String): ScheduleSettingsTable? {
        val listType = object: TypeToken<ScheduleSettingsTable>(){}.type
        return Gson().fromJson(scheduleSettings, listType)
    }

}