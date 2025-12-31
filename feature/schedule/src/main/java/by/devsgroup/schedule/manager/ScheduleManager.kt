package by.devsgroup.schedule.manager

import by.devsgroup.domain.model.schedule.full.FullSchedule
import by.devsgroup.domain.model.schedule.full.FullScheduleDay
import by.devsgroup.domain.model.schedule.full.FullScheduleLesson
import by.devsgroup.domain.model.schedule.template.ScheduleTemplate
import by.devsgroup.schedule.ext.parseDate
import by.devsgroup.schedule.mapper.ScheduleLessonTemplateToFullMapper
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/**
 * Template (6 days) -> Full Schedule (whole semester)
 */
class ScheduleManager(
    private val currentWeek: Int,
    private val scheduleTemplate: ScheduleTemplate
) {

    fun getFullSchedule(): FullSchedule {
        val scheduleLessonTemplateToFullMapper = ScheduleLessonTemplateToFullMapper()

        val scheduleStartDate = scheduleTemplate.startDate?.let { parseDate(it) }
        val scheduleEndDate = scheduleTemplate.endDate?.let { parseDate(it) }
        val schedulesLessons = scheduleTemplate.schedules?.map { scheduleLessonTemplateToFullMapper.map(it) }
        val schedules = schedulesLessons?.let {
            if (scheduleStartDate != null && scheduleEndDate != null) {
                getDaysFromLessons(
                    startDate = scheduleStartDate,
                    endDate = scheduleEndDate,
                    lessons = it
                )
            } else {
                listOf()
            }
        }

        val nextScheduleStartDate = scheduleTemplate.startDate?.let { parseDate(it) }
        val nextScheduleEndDate = scheduleTemplate.endDate?.let { parseDate(it) }
        val nextSchedulesLessons = scheduleTemplate.nextSchedules?.map { scheduleLessonTemplateToFullMapper.map(it) }
        val nextSchedules = nextSchedulesLessons?.let {
            if (nextScheduleStartDate != null && nextScheduleEndDate != null) {
                getDaysFromLessons(
                    startDate = nextScheduleStartDate,
                    endDate = nextScheduleEndDate,
                    lessons = it
                )
            } else {
                listOf()
            }
        }

        val examStartDate = scheduleTemplate.startDate?.let { parseDate(it) }
        val examEndDate = scheduleTemplate.endDate?.let { parseDate(it) }
        val examsLessons = scheduleTemplate.exams?.map { scheduleLessonTemplateToFullMapper.map(it) }
        val exams = examsLessons?.let {
            if (examStartDate != null && examEndDate != null) {
                getDaysFromLessons(
                    startDate = examStartDate,
                    endDate = examEndDate,
                    lessons = it
                )
            } else {
                listOf()
            }
        }

        return FullSchedule(
            startDate = scheduleTemplate.startDate,
            endDate = scheduleTemplate.endDate,
            startExamsDate = scheduleTemplate.startExamsDate,
            endExamsDate = scheduleTemplate.endExamsDate,
            employee = scheduleTemplate.employeeDto,
            group = scheduleTemplate.studentGroupDto,
            schedules = schedules,
            nextSchedules = nextSchedules,
            currentTerm = scheduleTemplate.currentTerm,
            nextTerm = scheduleTemplate.nextTerm,
            exams = exams,
            currentPeriod = scheduleTemplate.currentPeriod,
            partTimeOrRemote = scheduleTemplate.partTimeOrRemote,
        )
    }

    private fun getDaysFromLessons(
        startDate: LocalDate,
        endDate: LocalDate,
        lessons: List<FullScheduleLesson>
    ): List<FullScheduleDay> {
        val today = LocalDate.now()
        val result = mutableListOf<FullScheduleDay>()

        var currentDate = if (today.isAfter(startDate)) today else startDate

        while (!currentDate.isAfter(endDate)) {
            val weeksSinceStart = ChronoUnit.WEEKS.between(startDate, currentDate).toInt()
            val weekNumber = ((currentWeek - 1 + weeksSinceStart) % 4) + 1

            val dayOfWeek = currentDate.dayOfWeek
            if (dayOfWeek != DayOfWeek.SUNDAY) {
                val dayLessons = lessons.filter { lesson ->
                    lesson.dayOfWeek == dayOfWeek && lesson.weekNumber.contains(weekNumber)
                }

                result.add(
                    FullScheduleDay(
                        lessons = dayLessons.ifEmpty { emptyList() },
                        date = currentDate.atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli(),
                        week = weekNumber
                    )
                )
            }

            currentDate = currentDate.plusDays(1)
        }

        return result
    }

}


