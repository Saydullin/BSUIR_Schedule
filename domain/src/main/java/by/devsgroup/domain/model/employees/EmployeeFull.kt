package by.devsgroup.domain.model.employees

data class EmployeeFull(
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
    val departmentsId: String,
)


