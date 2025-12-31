package by.devsgroup.schedule.mapper

import by.devsgroup.database.schedule.entity.ScheduleEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.full.FullSchedule
import javax.inject.Inject

class ScheduleToEntityMapper @Inject constructor(
    private val scheduleEmployeeToEntityMapper: ScheduleEmployeeToEntityMapper,
    private val scheduleGroupToEntityMapper: ScheduleGroupToEntityMapper,
): Mapper<FullSchedule, ScheduleEntity> {

    override fun map(from: FullSchedule): ScheduleEntity {
        val scheduleId = from.employee?.id ?: from.group?.id ?: 0

        return ScheduleEntity(
            scheduleId = scheduleId,
            startDate = from.startDate,
            endDate = from.endDate,
            startExamsDate = from.startExamsDate,
            endExamsDate = from.endExamsDate,
            currentTerm = from.currentTerm,
            nextTerm = from.nextTerm,
            currentPeriod = from.currentPeriod,
            partTimeOrRemote = from.partTimeOrRemote,
            employee = from.employee?.let { scheduleEmployeeToEntityMapper.map(it) },
            group = from.group?.let { scheduleGroupToEntityMapper.map(it) },
        )
    }

}