package by.devsgroup.domain.model.schedule.template

import by.devsgroup.domain.model.schedule.common.ScheduleEmployee
import by.devsgroup.domain.model.schedule.common.ScheduleGroup

data class ScheduleTemplate(
    val startDate: String?,
    val endDate: String?,
    val startExamsDate: String?,
    val endExamsDate: String?,
    val employeeDto: ScheduleEmployee?,
    val studentGroupDto: ScheduleGroup?,
    val schedules: List<ScheduleLessonTemplate>?,
    val nextSchedules: List<ScheduleLessonTemplate>?,
    val currentTerm: String?,
    val nextTerm: String?,
    val exams: List<ScheduleLessonTemplate>?,
    val currentPeriod: String?,
    val partTimeOrRemote: Boolean?,
)


