package com.bsuir.domain.model.schedule.template

import com.bsuir.domain.model.schedule.common.ScheduleLessonEmployee
import com.bsuir.domain.model.schedule.common.ScheduleLessonGroup
import java.time.DayOfWeek

data class ScheduleLessonTemplate(
    val audiences: List<String>?,
    val dayOfWeek: DayOfWeek?,
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


