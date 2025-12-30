package by.devsgroup.domain.model.schedule.full

import by.devsgroup.domain.model.schedule.common.ScheduleEmployee
import by.devsgroup.domain.model.schedule.common.ScheduleGroup

data class FullSchedule(
    val startDate: String?,
    val endDate: String?,
    val startExamsDate: String?,
    val endExamsDate: String?,
    val employeeDto: ScheduleEmployee?,
    val studentGroupDto: ScheduleGroup?,
    val schedules: List<FullScheduleDay>?,
    val nextSchedules: List<FullScheduleDay>?,
    val currentTerm: String?,
    val nextTerm: String?,
    val exams: List<FullScheduleDay>?,
    val currentPeriod: String?,
    val partTimeOrRemote: Boolean?,
)


