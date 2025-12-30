package by.devsgroup.database.schedule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.devsgroup.database.schedule.entity.ScheduleEntity

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM `schedule` WHERE `group_name` = :groupName")
    fun getGroupSchedule(groupName: String): ScheduleEntity?

    @Query("SELECT * FROM `schedule` WHERE `employee_urlId` = :urlId")
    fun getEmployeeSchedule(urlId: String): ScheduleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(schedule: ScheduleEntity): Long

    @Query("DELETE FROM `schedule`")
    fun clear(): Int

}