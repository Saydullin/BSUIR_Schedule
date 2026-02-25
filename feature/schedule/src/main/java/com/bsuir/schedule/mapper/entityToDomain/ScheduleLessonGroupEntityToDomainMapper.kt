package com.bsuir.schedule.mapper.entityToDomain

import com.bsuir.database.schedule.entity.ScheduleLessonGroupEntity
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.schedule.common.ScheduleLessonGroup
import javax.inject.Inject

class ScheduleLessonGroupEntityToDomainMapper @Inject constructor(
): Mapper<ScheduleLessonGroupEntity, ScheduleLessonGroup> {

    override fun map(from: ScheduleLessonGroupEntity): ScheduleLessonGroup {
        return ScheduleLessonGroup(
            specialityName = from.specialityName,
            specialityCode = from.specialityCode,
            numberOfStudents = from.numberOfStudents,
            name = from.name,
            educationDegree = from.educationDegree,
        )
    }

}