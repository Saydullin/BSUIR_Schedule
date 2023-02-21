package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsuir.bsuirschedule.domain.models.WidgetSettings

@Entity
data class WidgetSettingsTable(
    @PrimaryKey val id: Int,
    @ColumnInfo val scheduleId: Int,
    @ColumnInfo val isDarkTheme: Boolean
) {

    fun toWidgetSettings() = WidgetSettings(
        id = id,
        scheduleId = scheduleId,
        isDarkTheme = isDarkTheme
    )

}