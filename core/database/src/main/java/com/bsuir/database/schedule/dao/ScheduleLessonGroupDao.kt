package com.bsuir.database.schedule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.bsuir.database.schedule.entity.ScheduleLessonGroupEntity

@Dao
interface ScheduleLessonGroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(groups: List<ScheduleLessonGroupEntity>): List<Long>

}