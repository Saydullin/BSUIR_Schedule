package by.devsgroup.week.mapper

import by.devsgroup.database.week.entity.WeekEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.week.Week
import javax.inject.Inject

class WeekEntityToDomainMapper @Inject constructor(
): Mapper<WeekEntity, Week> {

    override fun map(from: WeekEntity): Week {
        return Week(
            week = from.week,
            dateFrom = from.createdAt,
        )
    }

}