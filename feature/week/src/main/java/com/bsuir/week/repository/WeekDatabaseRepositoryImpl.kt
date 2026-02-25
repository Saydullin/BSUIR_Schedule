package com.bsuir.week.repository

import com.bsuir.database.week.dao.WeekDao
import com.bsuir.domain.model.week.Week
import com.bsuir.domain.repository.week.WeekDatabaseRepository
import com.bsuir.resource.Resource
import com.bsuir.week.mapper.WeekEntityToDomainMapper
import com.bsuir.week.mapper.WeekToEntityMapper
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