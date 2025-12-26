package by.devsgroup.employees.ui.model

data class EmployeeUI(
    val uniqueListId: Long,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val degree: String?,
    val rank: String?,
    val photoLink: String?,
    val calendarId: String?,
    val academicDepartment: List<String>?,
    val id: Int?,
    val urlId: String?,
    val fio: String?,
)
