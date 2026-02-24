package by.devsgroup.database.schedule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import by.devsgroup.database.schedule.entity.ScheduleEntity
import by.devsgroup.database.schedule.relation.ScheduleWithDays

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM `schedule`")
    fun getAllSchedules(): List<ScheduleEntity>

    @Query("SELECT `scheduleId` FROM `schedule` WHERE `employee_id` = :employeeId")
    fun getScheduleIdByEmployeeId(employeeId: Long): Long?

    @Query("SELECT `scheduleId` FROM `schedule` WHERE `group_id` = :groupId")
    fun getScheduleIdByGroupId(groupId: Long): Long?

    @Transaction
    @Query("SELECT * FROM `schedule` WHERE `scheduleId` = :scheduleId")
    fun getSchedule(scheduleId: Long): ScheduleEntity?

    @Transaction
    @Query("SELECT * FROM schedule WHERE scheduleId = :scheduleId")
    fun getFullSchedule(scheduleId: Long): ScheduleWithDays?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(schedule: ScheduleEntity): Long

    @Query("DELETE FROM `schedule` WHERE `group_id` = :groupId")
    fun deleteScheduleByGroupId(groupId: Long)

    @Query("DELETE FROM `schedule` WHERE `employee_id` = :employeeId")
    fun deleteScheduleByEmployeeId(employeeId: Long)

    @Query("DELETE FROM `schedule`")
    fun clear(): Int

}


