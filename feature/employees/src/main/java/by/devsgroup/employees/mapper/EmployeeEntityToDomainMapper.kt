package by.devsgroup.employees.mapper

import by.devsgroup.database.employees.entity.EmployeeEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.employee.Employee
import javax.inject.Inject

class EmployeeEntityToDomainMapper @Inject constructor(
): Mapper<EmployeeEntity, Employee> {

    override fun map(from: EmployeeEntity): Employee {
        return Employee(
            firstName = from.firstName,
            lastName = from.lastName,
            middleName = from.middleName,
            degree = from.degree,
            rank = from.rank,
            photoLink = from.photoLink,
            calendarId = from.calendarId,
            academicDepartment = listOf(),
            id = from.id,
            urlId = from.urlId,
            fio = from.fio,
        )
    }

}


