package com.bsuir.domain.repository.week

import com.bsuir.domain.model.week.Week
import com.bsuir.resource.Resource

interface WeekDatabaseRepository {

    suspend fun getCurrentWeek(): Resource<Week?>

    suspend fun saveCurrentWeek(week: Week): Resource<Unit>

}


