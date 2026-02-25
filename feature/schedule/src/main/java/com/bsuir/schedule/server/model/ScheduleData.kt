package com.bsuir.schedule.server.model

import com.google.gson.annotations.SerializedName

data class ScheduleData(
    @SerializedName("startDate")
    val startDate: String?,

    @SerializedName("endDate")
    val endDate: String?,

    @SerializedName("startExamsDate")
    val startExamsDate: String?,

    @SerializedName("endExamsDate")
    val endExamsDate: String?,

    @SerializedName("employeeDto")
    val employeeDto: ScheduleEmployeeData?,

    @SerializedName("studentGroupDto")
    val studentGroupDto: ScheduleGroupData?,

    @SerializedName("schedules")
    val schedules: ScheduleWeekData?,

    @SerializedName("nextSchedules")
    val nextSchedules: ScheduleWeekData?,

    @SerializedName("currentTerm")
    val currentTerm: String?,

    @SerializedName("nextTerm")
    val nextTerm: String?,

    @SerializedName("exams")
    val exams: List<ScheduleLessonData>?,

    @SerializedName("currentPeriod")
    val currentPeriod: String?,

    @SerializedName("isZaochOrDist")
    val isZaochOrDist: Boolean?,
)



