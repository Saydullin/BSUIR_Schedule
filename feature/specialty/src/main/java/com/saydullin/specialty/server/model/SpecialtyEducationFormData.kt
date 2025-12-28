package com.saydullin.specialty.server.model

import com.google.gson.annotations.SerializedName

data class SpecialtyEducationFormData(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String
)
