package by.devsgroup.domain.model.schedule.template

import by.devsgroup.domain.model.schedule.common.ScheduleLessonEmployee
import by.devsgroup.domain.model.schedule.common.ScheduleLessonGroup

data class ScheduleLessonTemplate(
    val audiences: List<String>?,
    val endLessonTime: String?,
    val startLessonTime: String?,
    val lessonTypeAbbrev: String?,
    val studentGroups: List<ScheduleLessonGroup>?,
    val subject: String?,
    val subjectFullName: String?,
    val weekNumber: List<Int>?,
    val employees: List<ScheduleLessonEmployee>?,
    val dateLesson: String?,
    val startLessonDate: String?,
    val endLessonDate: String?,
    val announcement: String?,
    val split: Boolean?,
)


