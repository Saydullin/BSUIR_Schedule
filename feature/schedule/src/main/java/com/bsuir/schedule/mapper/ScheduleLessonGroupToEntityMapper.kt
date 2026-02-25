package com.bsuir.schedule.mapper

import com.bsuir.database.schedule.entity.ScheduleLessonGroupEntity
import com.bsuir.domain.mapper.MapperWithContext
import com.bsuir.domain.model.schedule.common.ScheduleLessonGroup
import javax.inject.Inject

class ScheduleLessonGroupToEntityMapper @Inject constructor(
): MapperWithContext<ScheduleLessonGroup, ScheduleLessonGroupEntity, String> {

    override fun map(from: ScheduleLessonGroup, context: String): ScheduleLessonGroupEntity {
        return ScheduleLessonGroupEntity(
            lessonId = context,
            specialityName = from.specialityName,
            specialityCode = from.specialityCode,
            numberOfStudents = from.numberOfStudents,
            name = from.name,
            educationDegree = from.educationDegree,
        )
    }

}


