package by.devsgroup.week.repository

import by.devsgroup.database.week.dao.WeekDao
import by.devsgroup.domain.model.week.Week
import by.devsgroup.domain.repository.week.WeekDatabaseRepository
import by.devsgroup.resource.Resource
import by.devsgroup.week.mapper.WeekEntityToDomainMapper
import by.devsgroup.week.mapper.WeekToEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeekDatabaseRepositoryImpl @Inject constructor(
    private val weekDao: WeekDao,
    private val weekToEntityMapper: WeekToEntityMapper,
    private val weekEntityToDomainMapper: WeekEntityToDomainMapper,
): WeekDatabaseRepository {

    override suspend fun getCurrentWeek(): Resource<Week?> {
        return Resource.tryWithSuspend {
            val weekEntity = withContext(Dispatchers.IO) {
                weekDao.getWeek()
            }

            weekEntity?.let { weekEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun saveCurrentWeek(week: Week): Resource<Unit> {
        return Resource.tryWithSuspend {
            val weekEntity = weekToEntityMapper.map(week)

            withContext(Dispatchers.IO) {
                weekDao.save(weekEntity)
            }
        }
    }

}