package by.devsgroup.schedule.server.model

import by.devsgroup.domain.model.employees.Employee
import by.devsgroup.domain.model.groups.Group

data class ScheduleData(
    val startDate: String?,
    val endDate: String?,
    val startExamsDate: String?,
    val endExamsDate: String?,
    val employeeDto: Employee?,
    val studentGroupDto: Group?,
    val schedules: ScheduleWeekData?,
    val nextSchedules: ScheduleWeekData?,
    val currentTerm: String?,
    val nextTerm: String?,
    val exams: List<ScheduleLessonData>?,
    val currentPeriod: String?,
    val isZaochOrDist: Boolean?,
)


