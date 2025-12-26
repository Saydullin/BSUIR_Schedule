package by.devsgroup.employees.data.server.model

data class EmployeeData(
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
