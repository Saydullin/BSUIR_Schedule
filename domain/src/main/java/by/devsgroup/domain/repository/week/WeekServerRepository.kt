package by.devsgroup.domain.repository.week

import by.devsgroup.domain.model.week.Week
import by.devsgroup.resource.Resource

interface WeekServerRepository {

    suspend fun getCurrentWeek(): Resource<Week>

}