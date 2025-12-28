package by.devsgroup.groups.server.model

import com.google.gson.annotations.SerializedName

data class GroupData(
    @SerializedName("name")
    val name: String?,

    @SerializedName("facultyId")
    val facultyId: Int?,

    @SerializedName("facultyAbbrev")
    val facultyAbbrev: String?,

    @SerializedName("facultyName")
    val facultyName: String?,

    @SerializedName("specialityDepartmentEducationFormId")
    val specialityDepartmentEducationFormId: Int?,

    @SerializedName("specialityName")
    val specialityName: String?,

    @SerializedName("specialityAbbrev")
    val specialityAbbrev: String?,

    @SerializedName("course")
    val course: Int?,

    @SerializedName("id")
    val id: Int?,

    @SerializedName("calendarId")
    val calendarId: String?,

    @SerializedName("educationDegree")
    val educationDegree: Int?
)
