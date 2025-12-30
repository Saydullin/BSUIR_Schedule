package by.devsgroup.database.schedule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.devsgroup.database.schedule.entity.ScheduleDayEntity

@Dao
interface ScheduleDayDao {

    @Query("""
        SELECT * FROM `schedule_day`
        WHERE scheduleId = :scheduleId
        AND dateMillis >= :filterMillis
        ORDER BY dateMillis
        LIMIT :limit OFFSET :offset
        """)
    fun getPagingDays(
        filterMillis: Long,
        scheduleId: Long,
        limit: Int,
        offset: Int,
    ): List<ScheduleDayEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(day: ScheduleDayEntity): Long

}