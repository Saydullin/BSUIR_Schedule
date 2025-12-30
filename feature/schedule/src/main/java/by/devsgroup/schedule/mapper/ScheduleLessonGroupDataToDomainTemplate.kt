package by.devsgroup.schedule.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.common.ScheduleLessonGroup
import by.devsgroup.schedule.server.model.ScheduleLessonGroupData
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