package com.bsuir.bsuirschedule.data.db.dao

import androidx.room.*
import com.bsuir.bsuirschedule.data.db.entities.ScheduleTable

@Dao
interface GroupScheduleDao {

    @Query("SELECT * FROM ScheduleTable WHERE id = :groupId LIMIT 1")
    fun getGroupScheduleById(groupId: Int): ScheduleTable?

    @Query("SELECT * FROM ScheduleTable")
    fun getGroupSchedules(): List<ScheduleTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSchedule(schedule: ScheduleTable)

    @Query("DELETE FROM ScheduleTable WHERE group_name = :groupName")
    fun deleteGroupSchedule(groupName: String)

    @Query("DELETE FROM ScheduleTable WHERE employee_urlId = :employeeUrlId")
    fun deleteEmployeeSchedule(employeeUrlId: String)

}


