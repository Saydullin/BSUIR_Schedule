package com.bsuir.database.schedule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.bsuir.database.schedule.entity.ScheduleLessonEmployeeEntity

@Dao
interface ScheduleLessonEmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(employees: List<ScheduleLessonEmployeeEntity>): List<Long>

}