package com.saydullin.specialty.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.specialty.Specialty
import com.saydullin.specialty.server.model.SpecialtyData
import javax.inject.Inject

class SpecialtyDataToDomainMapper @Inject constructor(
    private val specialtyEducationFormDataToDomainMapper: SpecialtyEducationFormDataToDomainMapper
): Mapper<SpecialtyData, Specialty> {

    override fun map(from: SpecialtyData): Specialty {
        return Specialty(
            id = from.id,
            name = from.name,
            abbrev = from.abbrev,
            facultyId = from.facultyId,
            code = from.code,
            educationForm = specialtyEducationFormDataToDomainMapper.map(from.educationForm),
        )
    }

}