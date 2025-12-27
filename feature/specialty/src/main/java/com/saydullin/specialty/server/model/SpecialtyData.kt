package com.saydullin.specialty.server.model

data class SpecialtyData(
    val id: Long,
    val name: String,
    val abbrev: String,
    val facultyId: Long,
    val code: String,
    val educationForm: SpecialtyEducationFormData,
)


