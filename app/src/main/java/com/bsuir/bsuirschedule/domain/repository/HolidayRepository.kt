package com.bsuir.bsuirschedule.domain.repository

import com.bsuir.bsuirschedule.domain.models.Holiday
import com.bsuir.bsuirschedule.domain.utils.Resource

interface HolidayRepository {

    suspend fun getHolidays(): Resource<List<Holiday>>

    suspend fun getHolidayByDate(): Resource<Holiday>

}


