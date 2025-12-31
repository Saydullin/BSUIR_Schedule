package by.devsgroup.schedule.mapper.entityToDomain

import by.devsgroup.database.schedule.entity.ScheduleEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.preview.PreviewSchedule
import javax.inject.Inject

class ScheduleEntityToDomainMapper @Inject constructor(
): Mapper<ScheduleEntity, PreviewSchedule> {

    override fun map(from: ScheduleEntity): PreviewSchedule {
        return PreviewSchedule(
            startDate = from.startDate,
            endDate = from.endDate,
            startExamsDate = from.startExamsDate,
            endExamsDate = from.endExamsDate,
            employee = null,
            group = null,
            currentTerm = from.currentTerm,
            nextTerm = from.nextTerm,
            currentPeriod = from.currentPeriod,
            partTimeOrRemote = from.partTimeOrRemote,
        )
    }

}