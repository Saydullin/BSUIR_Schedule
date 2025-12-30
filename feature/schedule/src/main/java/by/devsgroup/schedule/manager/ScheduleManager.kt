package by.devsgroup.schedule.manager

import by.devsgroup.domain.model.schedule.full.FullScheduleDay
import by.devsgroup.domain.model.schedule.full.FullScheduleLesson
import by.devsgroup.domain.model.schedule.template.ScheduleTemplate
import by.devsgroup.schedule.mapper.ScheduleLessonTemplateToFullMapper
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

/**
 * Template (6 days) -> Full Schedule (whole semester)
 */
class ScheduleManager(
    private val currentWeek: Int,
    private val scheduleTemplate: ScheduleTemplate
) {

    fun createFullSchedule() {
        val scheduleLessonTemplateToFullMapper = ScheduleLessonTemplateToFullMapper()

        val lessons = scheduleTemplate.schedules?.map { scheduleLessonTemplateToFullMapper.map(it) }

        val days = lessons?.let { getDaysFromLessons(it) }

        println("createFullSchedule ${days?.size}")
    }

    private fun getDaysFromLessons(lessons: List<FullScheduleLesson>): List<FullScheduleDay> {
        val today = LocalDate.now()
        val result = mutableListOf<FullScheduleDay>()

        for (weekOffset in 0..3) {
            val weekNumber = ((currentWeek - 1 + weekOffset) % 4) + 1

            val weekStartDate = today.plusWeeks(weekOffset.toLong())

            DayOfWeek.entries
                .filter { it != DayOfWeek.SUNDAY }
                .forEach { dayOfWeek ->
                    val dayDate = weekStartDate.plusDays(
                        (dayOfWeek.value - weekStartDate.dayOfWeek.value).toLong()
                    )

                    if (weekOffset == 0 && dayDate.isBefore(today)) return@forEach

                    val dayLessons = lessons.filter { lesson ->
                        lesson.dayOfWeek == dayOfWeek && lesson.weekNumber.contains(weekNumber)
                    }

                    result.add(
                        FullScheduleDay(
                            lessons = dayLessons.ifEmpty { emptyList() },
                            date = dayDate.atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli(),
                            week = weekNumber
                        )
                    )
                }
        }

        return result
    }

}