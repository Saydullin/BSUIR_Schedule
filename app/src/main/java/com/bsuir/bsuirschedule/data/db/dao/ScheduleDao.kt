package com.bsuir.bsuirschedule.data.db.dao

import androidx.room.*
import com.bsuir.bsuirschedule.data.db.entities.ScheduleTable

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM ScheduleTable WHERE id = :id LIMIT 1")
    fun getGroupScheduleById(id: Int): ScheduleTable?

    @Query("SELECT * FROM ScheduleTable")
    fun getGroupSchedules(): List<ScheduleTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSchedule(schedule: ScheduleTable)

    @Query("UPDATE ScheduleTable SET settings = :newSettingsJSON WHERE id = :id")
    fun updateScheduleSettings(id: Int, newSettingsJSON: String)

    @Query("DELETE FROM ScheduleTable WHERE group_name = :groupName")
    fun deleteGroupSchedule(groupName: String)

    @Query("DELETE FROM ScheduleTable WHERE employee_urlId = :employeeUrlId")
    fun deleteEmployeeSchedule(employeeUrlId: String)

}


