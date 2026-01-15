package by.devsgroup.domain.model.schedule.full

import by.devsgroup.domain.model.schedule.common.ScheduleEmployee
import by.devsgroup.domain.model.schedule.common.ScheduleGroup
import by.devsgroup.domain.model.schedule.common.ScheduleLessonEmployee
import by.devsgroup.domain.model.schedule.common.ScheduleLessonGroup
import by.devsgroup.domain.model.schedule.common.ScheduleType
import java.time.DayOfWeek

data class FullScheduleLesson(
    val audiences: List<String>?,
    val dayOfWeek: DayOfWeek?,
    val endLessonTime: String?,
    val startLessonTime: String?,
    val lessonTypeAbbrev: String?,
    val studentGroups: List<ScheduleLessonGroup>?,
    val subject: String?,
    val scheduleType: ScheduleType,
    val subjectFullName: String?,
    val weekNumber: List<Int>,
    val employees: List<ScheduleLessonEmployee>?,
    val dateLesson: String?,
    val startLessonDate: String?,
    val endLessonDate: String?,
    val announcement: String?,
    val split: Boolean?,
    val scheduleGroup: ScheduleGroup?,
    val scheduleEmployee: ScheduleEmployee?,
)


