package by.devsgroup.database.schedule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import by.devsgroup.database.schedule.entity.ScheduleEmployeeEntity
import by.devsgroup.database.schedule.entity.ScheduleGroupEntity
import by.devsgroup.database.schedule.entity.ScheduleLessonEntity

@Dao
interface ScheduleGroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(group: ScheduleGroupEntity): Long

}