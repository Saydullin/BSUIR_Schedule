package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsuir.bsuirschedule.domain.models.Holiday

@Entity
data class HolidaysTable(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val title: String,
) {

    fun toHoliday(): Holiday {
        return Holiday(
            id = id,
            date = date,
            title = title,
        )
    }

}


