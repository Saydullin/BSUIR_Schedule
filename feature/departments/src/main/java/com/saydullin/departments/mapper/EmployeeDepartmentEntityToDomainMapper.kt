package com.saydullin.departments.mapper

import by.devsgroup.database.departments.entity.EmployeeDepartmentEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.department.Department
import javax.inject.Inject

class EmployeeDepartmentEntityToDomainMapper @Inject constructor(
): Mapper<EmployeeDepartmentEntity, Department> {

    override fun map(from: EmployeeDepartmentEntity): Department {
        return Department(
            id = from.id,
            name = from.name,
            abbrev = from.abbrev,
            urlId = from.urlId,
        )
    }

}


