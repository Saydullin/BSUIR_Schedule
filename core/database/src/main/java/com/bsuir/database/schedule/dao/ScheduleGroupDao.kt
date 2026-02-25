package com.bsuir.database.schedule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.bsuir.database.schedule.entity.ScheduleGroupEntity

@Dao
interface ScheduleGroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(group: ScheduleGroupEntity): Long

}