package by.devsgroup.schedule.server.model

data class ScheduleEmployeeData(
    val id: Long?,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val degree: String?,
    val degreeAbbrev: String?,
    val email: String?,
    val rank: String?,
    val photoLink: String?,
    val calendarId: String?,
    val jobPositions: String?,
    val chief: Boolean,
    val urlId: String?,
)