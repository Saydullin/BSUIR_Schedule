package com.saydullin.specialty.mapper

import by.devsgroup.database.specialty.entity.SpecialtyEducationFormEntity
import by.devsgroup.database.specialty.entity.SpecialtyEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.specialty.Specialty
import javax.inject.Inject

class SpecialtyToEntityMapper @Inject constructor(
): Mapper<Specialty, SpecialtyEntity> {

    override fun map(from: Specialty): SpecialtyEntity {
        return SpecialtyEntity(
            id = from.id,
            name = from.name,
            abbrev = from.abbrev,
            facultyId = from.facultyId,
            code = from.code,
            educationForm = SpecialtyEducationFormEntity(
                id = from.educationForm.id,
                name = from.educationForm.name,
            ),
        )
    }

}