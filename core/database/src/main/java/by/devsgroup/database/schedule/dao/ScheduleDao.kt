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

    @Transaction
    @Query("SELECT * FROM schedule WHERE scheduleId = :scheduleId")
    fun getSchedule(scheduleId: Long): ScheduleEntity?

    @Transaction
    @Query("SELECT * FROM schedule WHERE scheduleId = :scheduleId")
    fun getFullSchedule(scheduleId: Long): ScheduleWithDays?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(schedule: ScheduleEntity): Long

    @Query("DELETE FROM `schedule`")
    fun clear(): Int

}


