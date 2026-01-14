package by.devsgroup.week.mapper

import by.devsgroup.database.week.entity.WeekEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.week.Week
import javax.inject.Inject

class WeekToEntityMapper @Inject constructor(
): Mapper<Week, WeekEntity> {

    override fun map(from: Week): WeekEntity {
        return WeekEntity(
            week = from.week,
            createdAt = from.dateFrom,
        )
    }

}