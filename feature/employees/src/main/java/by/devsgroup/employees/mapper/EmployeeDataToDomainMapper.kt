package by.devsgroup.employees.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.employees.Employee
import by.devsgroup.employees.server.model.EmployeeData
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


