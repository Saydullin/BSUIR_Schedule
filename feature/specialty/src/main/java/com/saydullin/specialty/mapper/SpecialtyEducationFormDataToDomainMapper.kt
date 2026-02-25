package com.saydullin.specialty.mapper

import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.specialty.SpecialtyEducationForm
import com.saydullin.specialty.server.model.SpecialtyEducationFormData
import javax.inject.Inject

class SpecialtyEducationFormDataToDomainMapper @Inject constructor(
): Mapper<SpecialtyEducationFormData, SpecialtyEducationForm> {

    override fun map(from: SpecialtyEducationFormData): SpecialtyEducationForm {
        return SpecialtyEducationForm(
            id = from.id,
            name = from.name,
        )
    }

}