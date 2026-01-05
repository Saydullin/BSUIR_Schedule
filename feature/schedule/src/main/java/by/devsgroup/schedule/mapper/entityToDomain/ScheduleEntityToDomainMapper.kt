package by.devsgroup.schedule.mapper.entityToDomain

import by.devsgroup.database.schedule.entity.ScheduleEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.preview.PreviewSchedule
import by.devsgroup.schedule.mapper.ScheduleEmployeeToDomainMapper
import by.devsgroup.schedule.mapper.ScheduleGroupToDomainMapper
import javax.inject.Inject

class ScheduleEntityToDomainMapper @Inject constructor(
    private val scheduleEmployeeToDomainMapper: ScheduleEmployeeToDomainMapper,
    private val scheduleGroupToDomainMapper: ScheduleGroupToDomainMapper,
): Mapper<ScheduleEntity, PreviewSchedule> {

    override fun map(from: ScheduleEntity): PreviewSchedule {
        return PreviewSchedule(
            startDate = from.startDate,
            endDate = from.endDate,
            startExamsDate = from.startExamsDate,
            endExamsDate = from.endExamsDate,
            employee = from.employee?.let { scheduleEmployeeToDomainMapper.map(it) },
            group = from.group?.let { scheduleGroupToDomainMapper.map(it) },
            currentTerm = from.currentTerm,
            nextTerm = from.nextTerm,
            currentPeriod = from.currentPeriod,
            partTimeOrRemote = from.partTimeOrRemote,
        )
    }

}