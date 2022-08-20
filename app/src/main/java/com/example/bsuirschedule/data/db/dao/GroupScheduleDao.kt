package com.example.bsuirschedule.data.db.dao

import androidx.room.*
import com.example.bsuirschedule.data.db.entities.GroupScheduleTable

@Dao
interface GroupScheduleDao {

    @Query("SELECT * FROM GroupScheduleTable WHERE id = :groupId LIMIT 1")
    fun getGroupScheduleById(groupId: Int): GroupScheduleTable?

    @Query("SELECT * FROM GroupScheduleTable")
    fun getGroupSchedules(): List<GroupScheduleTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSchedule(schedule: GroupScheduleTable)

    @Query("DELETE FROM GroupScheduleTable WHERE group_name = :groupName")
    fun deleteGroupSchedule(groupName: String)

    @Query("DELETE FROM GroupScheduleTable WHERE employee_urlId = :employeeUrlId")
    fun deleteEmployeeSchedule(employeeUrlId: String)

}


