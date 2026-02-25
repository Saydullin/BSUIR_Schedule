package com.saydullin.specialty.mapper

import com.bsuir.database.specialty.entity.SpecialtyEntity
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.specialty.Specialty
import com.bsuir.domain.model.specialty.SpecialtyEducationForm
import javax.inject.Inject

class SpecialtyEntityToDomainMapper @Inject constructor(
): Mapper<SpecialtyEntity, Specialty> {

    override fun map(from: SpecialtyEntity): Specialty {
        return Specialty(
            id = from.id,
            name = from.name,
            abbrev = from.abbrev,
            facultyId = from.facultyId,
            code = from.code,
            educationForm = SpecialtyEducationForm(
                id = from.educationForm.id,
                name = from.educationForm.name,
            ),
        )
    }

}