package com.bsuir.employees.mapper

import com.bsuir.database.employees.entity.EmployeeEntity
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.employee.Employee
import javax.inject.Inject

class EmployeeToEntityMapper @Inject constructor(
): Mapper<Employee, EmployeeEntity> {

    override fun map(from: Employee): EmployeeEntity {
        val fullName = listOfNotNull(
            from.lastName,
            from.firstName,
            from.middleName
        )
            .takeIf { it.isNotEmpty() }
            ?.joinToString(" ")
            ?.lowercase()

        return EmployeeEntity(
            departmentKeyId = "", // TODO
            firstName = from.firstName,
            lastName = from.lastName,
            middleName = from.middleName,
            fullName = fullName,
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


