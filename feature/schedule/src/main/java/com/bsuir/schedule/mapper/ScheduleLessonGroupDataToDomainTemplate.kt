package com.bsuir.schedule.mapper

import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.schedule.common.ScheduleLessonGroup
import com.bsuir.schedule.server.model.ScheduleLessonGroupData
import javax.inject.Inject

class ScheduleLessonGroupDataToDomainTemplate @Inject constructor(
): Mapper<ScheduleLessonGroupData, ScheduleLessonGroup> {

    override fun map(from: ScheduleLessonGroupData): ScheduleLessonGroup {
        return ScheduleLessonGroup(
            specialityName = from.specialityName,
            specialityCode = from.specialityCode,
            numberOfStudents = from.numberOfStudents,
            name = from.name,
            educationDegree = from.educationDegree,
        )
    }

}