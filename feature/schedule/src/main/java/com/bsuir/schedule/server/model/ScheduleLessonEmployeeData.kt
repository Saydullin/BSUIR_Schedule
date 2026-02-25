package com.bsuir.schedule.server.model

import com.google.gson.annotations.SerializedName

data class ScheduleLessonEmployeeData(
    @SerializedName("id")
    val id: Long?,

    @SerializedName("firstName")
    val firstName: String?,

    @SerializedName("lastName")
    val lastName: String?,

    @SerializedName("middleName")
    val middleName: String?,

    @SerializedName("degree")
    val degree: String?,

    @SerializedName("degreeAbbrev")
    val degreeAbbrev: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("rank")
    val rank: String?,

    @SerializedName("photoLink")
    val photoLink: String?,

    @SerializedName("calendarId")
    val calendarId: String?,

    @SerializedName("jobPositions")
    val jobPositions: String?,

    @SerializedName("chief")
    val chief: Boolean,

    @SerializedName("urlId")
    val urlId: String?,
)

