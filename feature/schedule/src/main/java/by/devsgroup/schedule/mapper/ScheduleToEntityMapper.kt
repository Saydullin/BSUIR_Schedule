package by.devsgroup.schedule.mapper

import by.devsgroup.database.schedule.entity.ScheduleEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.template.ScheduleTemplate
import javax.inject.Inject

class ScheduleToEntityMapper @Inject constructor(
    private val scheduleEmployeeToEntityMapper: ScheduleEmployeeToEntityMapper,
    private val scheduleGroupToEntityMapper: ScheduleGroupToEntityMapper,
): Mapper<ScheduleTemplate, ScheduleEntity> {

    override fun map(from: ScheduleTemplate): ScheduleEntity {
        return ScheduleEntity(
            startDate = from.startDate,
            endDate = from.endDate,
            startExamsDate = from.startExamsDate,
            endExamsDate = from.endExamsDate,
            currentTerm = from.currentTerm,
            nextTerm = from.nextTerm,
            currentPeriod = from.currentPeriod,
            partTimeOrRemote = from.partTimeOrRemote,
            employee = from.employeeDto?.let { scheduleEmployeeToEntityMapper.map(it) },
            group = from.studentGroupDto?.let { scheduleGroupToEntityMapper.map(it) },
        )
    }

}