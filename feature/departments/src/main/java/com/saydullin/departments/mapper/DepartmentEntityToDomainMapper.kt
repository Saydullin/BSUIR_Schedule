package com.saydullin.departments.mapper

import com.bsuir.database.departments.entity.DepartmentEntity
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.department.Department
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


