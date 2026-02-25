package com.bsuir.week.mapper

import com.bsuir.database.week.entity.WeekEntity
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.week.Week
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