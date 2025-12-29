package by.devsgroup.schedule.mapper

import by.devsgroup.database.schedule.entity.ScheduleEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.Schedule
import javax.inject.Inject

class ScheduleToEntityMapper @Inject constructor(
): Mapper<Schedule, ScheduleEntity> {

    override fun map(from: Schedule): ScheduleEntity {
        return ScheduleEntity(
            startDate = from.startDate,
            endDate = from.endDate,
            startExamsDate = from.startExamsDate,
            endExamsDate = from.endExamsDate,
            currentTerm = from.currentTerm,
            nextTerm = from.nextTerm,
            currentPeriod = from.currentPeriod,
            partTimeOrRemote = from.partTimeOrRemote,
            employee = null,
            group = null,
        )
    }

}