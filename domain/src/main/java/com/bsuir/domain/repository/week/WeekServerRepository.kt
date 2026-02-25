package com.bsuir.domain.repository.week

import com.bsuir.domain.model.week.Week
import com.bsuir.resource.Resource

interface WeekServerRepository {

    suspend fun getCurrentWeek(): Resource<Week>

}