package com.bsuir.employees.mapper

import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.employee.Employee
import com.bsuir.employees.server.model.EmployeeData
import javax.inject.Inject

class EmployeeDataToDomainMapper @Inject constructor(
): Mapper<EmployeeData, Employee> {

    override fun map(from: EmployeeData): Employee {
        return Employee(
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


