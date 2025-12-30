package by.devsgroup.schedule.server.model

data class ScheduleGroupData(
    val id: Long?,
    val name: String?,
    val facultyId: Int?,
    val facultyAbbrev: String?,
    val facultyName: String?,
    val specialityDepartmentEducationFormId: Int?,
    val specialityName: String?,
    val specialityAbbrev: String?,
    val course: Int?,
    val calendarId: String?,
    val educationDegree: Int?,
)