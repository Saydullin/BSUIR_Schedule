package by.devsgroup.employees.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.employees.data.db.entity.EmployeeEntity
import by.devsgroup.employees.ui.model.EmployeeUI
import javax.inject.Inject

class EmployeeEntityToUiMapper @Inject constructor(
): Mapper<EmployeeEntity, EmployeeUI> {

    override fun map(from: EmployeeEntity): EmployeeUI {
        return EmployeeUI(
            uniqueListId = from.tableId,
            firstName = from.firstName,
            lastName = from.lastName,
            middleName = from.middleName,
            degree = from.degree,
            rank = from.rank,
            photoLink = from.photoLink,
            calendarId = from.calendarId,
            academicDepartment = from.academicDepartment,
            id = from.id,
            urlId = from.urlId,
            fio = from.fio,
        )
    }

}