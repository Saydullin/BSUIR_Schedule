package com.saydullin.faculty.mapper

import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.faculty.Faculty
import com.saydullin.faculty.server.model.FacultyData
import javax.inject.Inject

class FacultyDataToDomainMapper @Inject constructor(
): Mapper<FacultyData, Faculty> {

    override fun map(from: FacultyData): Faculty {
        return Faculty(
            id = from.id,
            name = from.name,
            abbrev = from.abbrev,
        )
    }

}


