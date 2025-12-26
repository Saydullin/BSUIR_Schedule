package com.saydullin.departments.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.departments.Department
import com.saydullin.departments.data.db.entity.DepartmentEntity
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


