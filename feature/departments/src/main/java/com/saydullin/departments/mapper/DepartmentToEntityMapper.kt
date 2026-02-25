package com.saydullin.departments.mapper

import com.bsuir.database.departments.entity.DepartmentEntity
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.department.Department
import javax.inject.Inject

class DepartmentToEntityMapper @Inject constructor(
): Mapper<Department, DepartmentEntity> {

    override fun map(from: Department): DepartmentEntity {
        return DepartmentEntity(
            id = from.id,
            name = from.name,
            abbrev = from.abbrev,
            urlId = from.urlId,
        )
    }

}


