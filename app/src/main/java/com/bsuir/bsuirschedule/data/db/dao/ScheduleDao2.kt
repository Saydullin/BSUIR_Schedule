package com.bsuir.bsuirschedule.data.db.dao

import androidx.room.*
import com.bsuir.bsuirschedule.data.db.entities.ScheduleTable2

@Dao
interface ScheduleDao2 {

    @Query("SELECT * FROM ScheduleTable2 WHERE id = :id LIMIT 1")
    fun getGroupScheduleById(id: Int): ScheduleTable2?

    @Query("SELECT * FROM ScheduleTable2")
    fun getGroupSchedules(): List<ScheduleTable2>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSchedule(schedule: ScheduleTable2)

    @Query("DELETE FROM ScheduleTable2 WHERE group_name = :groupName")
    fun deleteGroupSchedule(groupName: String)

    @Query("DELETE FROM ScheduleTable2 WHERE employee_urlId = :employeeUrlId")
    fun deleteEmployeeSchedule(employeeUrlId: String)

}


