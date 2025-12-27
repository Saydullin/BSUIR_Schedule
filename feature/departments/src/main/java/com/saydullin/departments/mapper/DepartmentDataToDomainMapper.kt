package com.saydullin.departments.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.departments.Department
import com.saydullin.departments.server.model.DepartmentData
import javax.inject.Inject

class DepartmentDataToDomainMapper @Inject constructor(
): Mapper<DepartmentData, Department> {

    override fun map(from: DepartmentData): Department {
        return Department(
            id = from.id,
            name = from.name,
            abbrev = from.abbrev,
            urlId = from.urlId,
        )
    }

}


