package by.devsgroup.schedule.mapper.entityToDomain

import by.devsgroup.database.schedule.entity.ScheduleLessonGroupEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.common.ScheduleLessonGroup
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