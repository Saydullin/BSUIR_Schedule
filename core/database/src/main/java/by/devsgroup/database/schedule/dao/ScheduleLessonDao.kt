package by.devsgroup.database.schedule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import by.devsgroup.database.schedule.entity.ScheduleLessonEntity

@Dao
interface ScheduleLessonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(lessons: List<ScheduleLessonEntity>): List<Long>

}