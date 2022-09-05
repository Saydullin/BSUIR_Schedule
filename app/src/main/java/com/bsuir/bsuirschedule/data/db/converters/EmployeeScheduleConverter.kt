package com.bsuir.bsuirschedule.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.bsuir.bsuirschedule.domain.models.EmployeeSubject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class EmployeeScheduleConverter {

    @TypeConverter
    fun fromGroupToString(employee: EmployeeSubject?): String? {
        return Gson().toJson(employee)
    }

    @TypeConverter
    fun fromGroupToString(employeeJSON: String): EmployeeSubject? {
        val listType = object: TypeToken<EmployeeSubject>(){}.type
        return Gson().fromJson(employeeJSON, listType)
    }

}