package com.bsuir.week.mapper

import com.bsuir.database.week.entity.WeekEntity
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.week.Week
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