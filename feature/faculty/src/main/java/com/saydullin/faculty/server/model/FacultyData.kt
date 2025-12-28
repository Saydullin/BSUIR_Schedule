package com.saydullin.faculty.server.model

import com.google.gson.annotations.SerializedName

data class FacultyData(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("abbrev")
    val abbrev: String
)



