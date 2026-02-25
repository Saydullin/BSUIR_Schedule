package com.bsuir.database.week.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bsuir.database.week.entity.WeekEntity

@Dao
interface WeekDao {

    @Query("SELECT * FROM week WHERE tableId = 1")
    fun getWeek(): WeekEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(week: WeekEntity): Long

}