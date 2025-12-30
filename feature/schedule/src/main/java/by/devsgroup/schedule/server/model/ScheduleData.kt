package by.devsgroup.schedule.server.model

data class ScheduleData(
    val startDate: String?,
    val endDate: String?,
    val startExamsDate: String?,
    val endExamsDate: String?,
    val employeeDto: ScheduleEmployeeData?,
    val studentGroupDto: ScheduleGroupData?,
    val schedules: ScheduleWeekData?,
    val nextSchedules: ScheduleWeekData?,
    val currentTerm: String?,
    val nextTerm: String?,
    val exams: List<ScheduleLessonData>?,
    val currentPeriod: String?,
    val isZaochOrDist: Boolean?,
)


