package by.devsgroup.database.schedule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import by.devsgroup.database.schedule.entity.ScheduleLessonEmployeeEntity

@Dao
interface ScheduleLessonEmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(employees: List<ScheduleLessonEmployeeEntity>): List<Long>

}