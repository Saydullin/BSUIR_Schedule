package com.bsuir.bsuirschedule.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bsuir.bsuirschedule.data.db.entities.WidgetSettingsTable

@Dao
interface WidgetSettingsDao {

    @Query("SELECT * FROM WidgetSettingsTable WHERE id=:widgetId")
    fun getWidgetSettings(widgetId: Int): WidgetSettingsTable?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWidgetSettings(widgetSettingsTable: WidgetSettingsTable)

    @Query("DELETE FROM WidgetSettingsTable WHERE id=:widgetId")
    fun deleteWidgetSettings(widgetId: Int)

}


