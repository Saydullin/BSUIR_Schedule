package by.devsgroup.database.schedule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import by.devsgroup.database.schedule.entity.ScheduleLessonEmployeeEntity
import by.devsgroup.database.schedule.entity.ScheduleLessonGroupEntity

@Dao
interface ScheduleLessonGroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(groups: List<ScheduleLessonGroupEntity>): List<Long>

}