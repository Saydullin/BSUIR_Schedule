package by.devsgroup.employees.ui.model

import by.devsgroup.domain.model.department.Department

data class EmployeeUI(
    val uniqueListId: Long,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val degree: String?,
    val rank: String?,
    val photoLink: String?,
    val calendarId: String?,
    val departments: List<Department>,
    val id: Long?,
    val urlId: String?,
    val fio: String?,
) {

    fun getFullName(): String {
        return listOfNotNull(lastName, firstName, middleName)
            .joinToString(" ")
            .trim()
    }

}
