package com.saydullin.faculty.mapper

import com.bsuir.database.faculty.entity.FacultyEntity
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.faculty.Faculty
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