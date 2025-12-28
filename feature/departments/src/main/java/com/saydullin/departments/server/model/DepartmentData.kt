package com.saydullin.departments.server.model

import com.google.gson.annotations.SerializedName

data class DepartmentData(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("abbrev")
    val abbrev: String?,

    @SerializedName("urlId")
    val urlId: String?
)

