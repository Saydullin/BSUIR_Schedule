package by.devsgroup.schedule.server.model

import com.google.gson.annotations.SerializedName

data class ScheduleLessonData(
    @SerializedName("auditories")
    val audiences: List<String>?,

    @SerializedName("endLessonTime")
    val endLessonTime: String?,

    @SerializedName("startLessonTime")
    val startLessonTime: String?,

    @SerializedName("lessonTypeAbbrev")
    val lessonTypeAbbrev: String?,

    @SerializedName("studentGroups")
    val studentGroups: List<ScheduleLessonGroupData>?,

    @SerializedName("subject")
    val subject: String?,

    @SerializedName("subjectFullName")
    val subjectFullName: String?,

    @SerializedName("weekNumber")
    val weekNumber: List<Int>,

    @SerializedName("employees")
    val employees: List<ScheduleLessonEmployeeData>?,

    @SerializedName("dateLesson")
    val dateLesson: String?,

    @SerializedName("startLessonDate")
    val startLessonDate: String?,

    @SerializedName("endLessonDate")
    val endLessonDate: String?,

    @SerializedName("announcement")
    val announcement: String?,

    @SerializedName("split")
    val split: Boolean?,
)



