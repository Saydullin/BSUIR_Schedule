package by.devsgroup.employees.mapper

import by.devsgroup.database.employees.entity.EmployeeEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.employee.Employee
import javax.inject.Inject

class EmployeeToEntityMapper @Inject constructor(
): Mapper<Employee, EmployeeEntity> {

    override fun map(from: Employee): EmployeeEntity {
        return EmployeeEntity(
            departmentKeyId = "",
            firstName = from.firstName,
            lastName = from.lastName,
            middleName = from.middleName,
            degree = from.degree,
            rank = from.rank,
            photoLink = from.photoLink,
            calendarId = from.calendarId,
            id = from.id,
            urlId = from.urlId,
            fio = from.fio,
        )
    }

}


