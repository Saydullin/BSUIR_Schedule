package com.saydullin.departments.mapper

import by.devsgroup.database.departments.entity.DepartmentEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.departments.Department
import javax.inject.Inject

class DepartmentEntityToDomainMapper @Inject constructor(
): Mapper<DepartmentEntity, Department> {

    override fun map(from: DepartmentEntity): Department {
        return Department(
            id = from.id,
            name = from.name,
            abbrev = from.abbrev,
            urlId = from.urlId,
        )
    }

}


