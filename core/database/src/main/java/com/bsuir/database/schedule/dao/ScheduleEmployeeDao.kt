package com.bsuir.database.schedule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.bsuir.database.schedule.entity.ScheduleEmployeeEntity

@Dao
interface ScheduleEmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(employee: ScheduleEmployeeEntity): Long

}