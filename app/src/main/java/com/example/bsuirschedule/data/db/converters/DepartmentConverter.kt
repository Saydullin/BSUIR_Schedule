package com.example.bsuirschedule.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.bsuirschedule.data.db.entities.DepartmentTable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class DepartmentConverter {

    @TypeConverter
    fun fromDepListToString(departmentsList: List<DepartmentTable>?): String? {
        return Gson().toJson(departmentsList)
    }

    @TypeConverter
    fun fromStringToDepList(departmentsJSON: String): List<DepartmentTable>? {
        val listType = object: TypeToken<List<DepartmentTable>>(){}.type
        return Gson().fromJson(departmentsJSON, listType)
    }

}