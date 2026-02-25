package com.saydullin.faculty.mapper

import com.bsuir.database.faculty.entity.FacultyEntity
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.faculty.Faculty
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