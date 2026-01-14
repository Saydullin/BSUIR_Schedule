package by.devsgroup.domain.repository.week

import by.devsgroup.domain.model.week.Week
import by.devsgroup.resource.Resource

interface WeekDatabaseRepository {

    suspend fun getCurrentWeek(): Resource<Week?>

    suspend fun saveCurrentWeek(week: Week): Resource<Unit>

}


