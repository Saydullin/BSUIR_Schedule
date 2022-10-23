package com.bsuir.bsuirschedule.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class ScheduleSettingsConverter {

    @TypeConverter
    fun fromScheduleToString(scheduleSettings: ScheduleSettings?): String? {
        return Gson().toJson(scheduleSettings)
    }

    @TypeConverter
    fun fromStringToSchedule(scheduleSettings: String): ScheduleSettings? {
        val listType = object: TypeToken<ScheduleSettings>(){}.type
        return Gson().fromJson(scheduleSettings, listType)
    }

}


