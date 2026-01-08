package by.devsgroup.schedule.ui.model

sealed class PreviewScheduleType(
    val scheduleId: Long,
) {

    class Group(
        val id: Long,
        val name: String
    ): PreviewScheduleType(
        scheduleId = id
    )

    class Employee(
        val id: Long,
        val firstName: String,
        val middleName: String,
        val lastName: String,
        val urlId: String?,
        val image: String?,
    ): PreviewScheduleType(
        scheduleId = id
    )

}