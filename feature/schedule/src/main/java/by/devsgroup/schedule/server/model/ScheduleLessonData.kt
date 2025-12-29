package by.devsgroup.schedule.server.model

import com.google.gson.annotations.SerializedName

data class ScheduleLessonData(
    @SerializedName("auditories")
    val audiences: List<String>?,
    val endLessonTime: String,
    val startLessonTime: String,
    val lessonTypeAbbrev: String,
    val studentGroups: List<ScheduleLessonGroupData>?,
    val subject: String,
    val subjectFullName: String,
    val weekNumber: List<Int>,
    val employees: List<ScheduleLessonEmployeeData>?,
    val dateLesson: String?,
    val startLessonDate: String,
    val endLessonDate: String,
    val announcement: String,
    val split: Boolean,
)


