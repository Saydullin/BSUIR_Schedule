package com.bsuir.bsuirschedule.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.bsuir.bsuirschedule.data.db.entities.HolidaysTable

@Dao
interface HolidaysDao {

    @Query("SELECT * FROM HolidaysTable")
    fun getHolidays(): List<HolidaysTable>

    @Query("SELECT * FROM HolidaysTable WHERE date=:date")
    fun getHolidayByDate(date: Long): HolidaysTable?

}


