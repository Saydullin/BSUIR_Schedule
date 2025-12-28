package com.saydullin.faculty.mapper

import by.devsgroup.database.faculty.entity.FacultyEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.faculty.Faculty
import javax.inject.Inject

class FacultyToEntityMapper @Inject constructor(
): Mapper<Faculty, FacultyEntity> {

    override fun map(from: Faculty): FacultyEntity {
        return FacultyEntity(
            id = from.id,
            name = from.name,
            abbrev = from.abbrev,
        )
    }

}