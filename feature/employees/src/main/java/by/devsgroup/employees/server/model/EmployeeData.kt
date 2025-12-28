package by.devsgroup.employees.server.model

import com.google.gson.annotations.SerializedName

data class EmployeeData(
    @SerializedName("firstName")
    val firstName: String?,

    @SerializedName("lastName")
    val lastName: String?,

    @SerializedName("middleName")
    val middleName: String?,

    @SerializedName("degree")
    val degree: String?,

    @SerializedName("rank")
    val rank: String?,

    @SerializedName("photoLink")
    val photoLink: String?,

    @SerializedName("calendarId")
    val calendarId: String?,

    @SerializedName("academicDepartment")
    val academicDepartment: List<String>?,

    @SerializedName("id")
    val id: Int?,

    @SerializedName("urlId")
    val urlId: String?,

    @SerializedName("fio")
    val fio: String?
)

