package com.bsuir.week.repository

import com.bsuir.domain.model.week.Week
import com.bsuir.domain.repository.week.WeekServerRepository
import com.bsuir.resource.Resource
import com.bsuir.week.server.service.WeekService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeekServerRepositoryImpl @Inject constructor(
    private val weekService: WeekService
): WeekServerRepository {

    override suspend fun getCurrentWeek(): Resource<Week> {
        return Resource.tryWithSuspend {
            val currentWeek = withContext(Dispatchers.IO) { weekService.getCurrentWeek() }

            Week(
                week = currentWeek,
                dateFrom = System.currentTimeMillis()
            )
        }
    }

}


