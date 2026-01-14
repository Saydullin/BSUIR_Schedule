package by.devsgroup.week.repository

import by.devsgroup.domain.model.week.Week
import by.devsgroup.domain.repository.week.WeekServerRepository
import by.devsgroup.resource.Resource
import by.devsgroup.week.server.service.WeekService
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


