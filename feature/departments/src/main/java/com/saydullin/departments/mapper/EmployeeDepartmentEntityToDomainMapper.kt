package com.saydullin.departments.mapper

import com.bsuir.database.departments.entity.EmployeeDepartmentEntity
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.department.Department
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


