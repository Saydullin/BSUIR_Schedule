package by.devsgroup.schedule.ui.model

sealed class PreviewScheduleType() {

    class Group(
        val name: String
    ): PreviewScheduleType()

    class Employee(
        val firstName: String,
        val middleName: String,
        val lastName: String,
        val image: String?,
    ): PreviewScheduleType()

}