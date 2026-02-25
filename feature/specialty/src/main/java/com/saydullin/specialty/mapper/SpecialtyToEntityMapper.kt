package com.saydullin.specialty.mapper

import com.bsuir.database.specialty.entity.SpecialtyEducationFormEntity
import com.bsuir.database.specialty.entity.SpecialtyEntity
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.specialty.Specialty
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