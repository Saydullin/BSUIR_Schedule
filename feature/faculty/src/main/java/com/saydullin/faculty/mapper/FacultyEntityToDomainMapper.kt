package com.saydullin.faculty.mapper

import by.devsgroup.database.faculty.entity.FacultyEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.faculty.Faculty
import javax.inject.Inject

class FacultyEntityToDomainMapper @Inject constructor(
): Mapper<FacultyEntity, Faculty> {

    override fun map(from: FacultyEntity): Faculty {
        return Faculty(
            id = from.id,
            name = from.name,
            abbrev = from.abbrev,
        )
    }

}