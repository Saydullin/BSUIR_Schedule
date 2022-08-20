package com.example.bsuirschedule.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.bsuirschedule.domain.models.Group
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class GroupConverter {

    @TypeConverter
    fun fromGroupToString(group: Group?): String? {
        return Gson().toJson(group)
    }

    @TypeConverter
    fun fromStringToGroup(groupJSON: String): Group? {
        val listType = object: TypeToken<Group>(){}.type
        return Gson().fromJson(groupJSON, listType)
    }

}


