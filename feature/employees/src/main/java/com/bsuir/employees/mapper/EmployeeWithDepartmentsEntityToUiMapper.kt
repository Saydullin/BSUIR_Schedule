package com.bsuir.employees.mapper

import com.bsuir.database.employees.relation.EmployeeWithDepartments
import com.bsuir.domain.mapper.Mapper
import com.bsuir.employees.ui.model.EmployeeUI
import javax.inject.Inject

class EmployeeWithDepartmentsEntityToUiMapper @Inject constructor(
    private val employeeDepartmentEntityToDomainMapper: EmployeeDepartmentEntityToDomainMapper,
): Mapper<EmployeeWithDepartments, EmployeeUI> {

    override fun map(from: EmployeeWithDepartments): EmployeeUI {
        return EmployeeUI(
            uniqueListId = from.employee.tableId,
            firstName = from.employee.firstName,
            lastName = from.employee.lastName,
            middleName = from.employee.middleName,
            degree = from.employee.degree,
            rank = from.employee.rank,
            photoLink = from.employee.photoLink,
            calendarId = from.employee.calendarId,
            id = from.employee.id,
            urlId = from.employee.urlId,
            fio = from.employee.fio,
            departments = from.departments.map { employeeDepartmentEntityToDomainMapper.map(it) }
        )
    }

}


