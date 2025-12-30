package by.devsgroup.domain.model.schedule.full

data class FullScheduleLesson(
    val audiences: List<String>?,
    val endLessonTime: String,
    val startLessonTime: String,
    val lessonTypeAbbrev: String,
    val studentGroups: List<Boolean>?,
    val subject: String,
    val subjectFullName: String,
    val weekNumber: List<Int>,
    val employees: List<Boolean>?,
    val dateLesson: String?,
    val startLessonDate: String,
    val endLessonDate: String,
    val announcement: String,
    val split: Boolean,
)


