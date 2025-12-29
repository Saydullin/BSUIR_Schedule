package by.devsgroup.domain.model.schedule

import by.devsgroup.domain.model.employees.Employee
import by.devsgroup.domain.model.groups.Group

data class Schedule(
    val startDate: String?,
    val endDate: String?,
    val startExamsDate: String?,
    val endExamsDate: String?,
    val employeeDto: Employee?,
    val studentGroupDto: Group?,
    val schedules: List<ScheduleDay>?,
    val nextSchedules: List<ScheduleDay>?,
    val currentTerm: String?,
    val nextTerm: String?,
    val exams: List<ScheduleDay>?,
    val currentPeriod: String,
    val partTimeOrRemote: Boolean,
)
