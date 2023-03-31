package com.bsuir.bsuirschedule.domain.repository

import com.bsuir.bsuirschedule.domain.models.CurrentWeek
import com.bsuir.bsuirschedule.domain.utils.Resource

interface CurrentWeekRepository {

    suspend fun getCurrentWeekAPI(): Resource<CurrentWeek>

    suspend fun getCurrentWeek(): Resource<CurrentWeek>

    suspend fun saveCurrentWeek(currentWeek: CurrentWeek): Resource<Unit>

}


