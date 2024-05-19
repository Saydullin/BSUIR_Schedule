package com.bsuir.bsuirschedule.data.repository

import com.bsuir.bsuirschedule.data.db.dao.HolidaysDao
import com.bsuir.bsuirschedule.domain.models.Holiday
import com.bsuir.bsuirschedule.domain.repository.HolidayRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class HolidayRepositoryImpl(
    private val holidaysDao: HolidaysDao
) : HolidayRepository {

    override suspend fun getHolidays(): Resource<List<Holiday>> {
        val holidaysEntity = holidaysDao.getHolidays()
        val holidays = holidaysEntity.map { it.toHoliday() }
        return Resource.Success(holidays)
    }

    override suspend fun getHolidayByDate(): Resource<Holiday> {
        TODO("Not yet implemented")
    }

}


