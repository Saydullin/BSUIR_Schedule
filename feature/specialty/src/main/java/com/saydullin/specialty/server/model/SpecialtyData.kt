package com.saydullin.specialty.server.model

import com.google.gson.annotations.SerializedName

data class SpecialtyData(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("abbrev")
    val abbrev: String,

    @SerializedName("facultyId")
    val facultyId: Long,

    @SerializedName("code")
    val code: String,

    @SerializedName("educationForm")
    val educationForm: SpecialtyEducationFormData
)



