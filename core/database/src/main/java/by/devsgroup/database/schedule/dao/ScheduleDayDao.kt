package by.devsgroup.database.schedule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import by.devsgroup.database.schedule.entity.ScheduleDayEntity
import by.devsgroup.database.schedule.relation.DaysWithLessons

@Dao
interface ScheduleDayDao {

    @Transaction
    @Query("""
        SELECT * FROM `schedule_day`
        WHERE scheduleId = :scheduleId
        ORDER BY dateMillis
        LIMIT :limit OFFSET :offset
        """)
    fun getPagingDays(
        scheduleId: Long,
        limit: Int,
        offset: Int,
    ): List<DaysWithLessons>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(day: ScheduleDayEntity): Long

}