package com.bsuir.schedule.server.model

import com.google.gson.annotations.SerializedName

data class ScheduleLessonGroupData(
    @SerializedName("specialityName")
    val specialityName: String?,

    @SerializedName("specialityCode")
    val specialityCode: String?,

    @SerializedName("numberOfStudents")
    val numberOfStudents: Int?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("educationDegree")
    val educationDegree: Int?,
)

