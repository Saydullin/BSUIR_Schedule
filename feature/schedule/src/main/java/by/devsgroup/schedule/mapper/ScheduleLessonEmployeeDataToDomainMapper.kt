package by.devsgroup.schedule.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.employees.Employee
import by.devsgroup.schedule.server.model.ScheduleLessonEmployeeData
import javax.inject.Inject

class ScheduleLessonEmployeeDataToDomainMapper @Inject constructor(
): Mapper<ScheduleLessonEmployeeData, Employee> {

    override fun map(from: ScheduleLessonEmployeeData): Employee {
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


