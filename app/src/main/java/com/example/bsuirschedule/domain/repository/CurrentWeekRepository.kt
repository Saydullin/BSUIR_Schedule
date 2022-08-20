package com.example.bsuirschedule.domain.repository

import com.example.bsuirschedule.domain.models.CurrentWeek
import com.example.bsuirschedule.domain.utils.Resource

interface CurrentWeekRepository {

    suspend fun getCurrentWeekAPI(): Resource<CurrentWeek>

    suspend fun getCurrentWeek(): Resource<CurrentWeek>

    suspend fun saveCurrentWeek(currentWeek: CurrentWeek): Resource<Unit>

}


