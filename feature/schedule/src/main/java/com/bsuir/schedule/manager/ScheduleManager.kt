package com.bsuir.schedule.manager

import com.bsuir.domain.model.schedule.common.ScheduleType
import com.bsuir.domain.model.schedule.full.FullSchedule
import com.bsuir.domain.model.schedule.full.FullScheduleDay
import com.bsuir.domain.model.schedule.full.FullScheduleLesson
import com.bsuir.domain.model.schedule.template.ScheduleTemplate
import com.bsuir.schedule.ext.parseDate
import com.bsuir.schedule.mapper.ScheduleLessonTemplateToFullMapper
import com.bsuir.schedule.mapper.context.ScheduleLessonToFullMapperContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

/**
 * Template (6 days) -> Full Schedule (whole semester)
 */
class ScheduleManager(
    private val currentWeek: Int,
    private val scheduleType: ScheduleType,
    private val scheduleTemplate: ScheduleTemplate,
) {

    fun getFullSchedule(): FullSchedule {
        val scheduleLessonTemplateToFullMapper = ScheduleLessonTemplateToFullMapper()

        val currentScheduleGroup = scheduleTemplate.studentGroupDto
        val currentScheduleEmployee = scheduleTemplate.employeeDto

        val scheduleLessonToFullMapperContext = ScheduleLessonToFullMapperContext(
            group = currentScheduleGroup,
            employee = currentScheduleEmployee,
            scheduleType = scheduleType
        )

        val scheduleStartDate = scheduleTemplate.startDate?.let { parseDate(it) }
        val scheduleEndDate = scheduleTemplate.endDate?.let { parseDate(it) }
        val schedulesLessons = scheduleTemplate.schedules?.map {
            scheduleLessonTemplateToFullMapper.map(it, scheduleLessonToFullMapperContext)
        }
        val schedules = schedulesLessons?.let { lessons ->
            if (scheduleStartDate != null && scheduleEndDate != null) {
                buildFullScheduleDays(
                    lessons = lessons,
                    currentWeek = currentWeek,
                )
            } else {
                listOf()
            }
        }

        val nextScheduleStartDate = scheduleTemplate.startDate?.let { parseDate(it) }
        val nextScheduleEndDate = scheduleTemplate.endDate?.let { parseDate(it) }
        val nextSchedulesLessons = scheduleTemplate.nextSchedules?.map {
            scheduleLessonTemplateToFullMapper.map(it, scheduleLessonToFullMapperContext)
        }
        val nextSchedules = nextSchedulesLessons?.let { lessons ->
            if (nextScheduleStartDate != null && nextScheduleEndDate != null) {
                buildFullScheduleDays(
                    lessons = lessons,
                    currentWeek = currentWeek,
                )
            } else {
                listOf()
            }
        }

        val examStartDate = scheduleTemplate.startDate?.let { parseDate(it) }
        val examEndDate = scheduleTemplate.endDate?.let { parseDate(it) }
        val examsLessons = scheduleTemplate.exams?.map {
            scheduleLessonTemplateToFullMapper.map(it, scheduleLessonToFullMapperContext)
        }
        val exams = examsLessons?.let { lessons ->
            if (examStartDate != null && examEndDate != null) {
                buildFullScheduleDays(
                    lessons = lessons,
                    currentWeek = currentWeek,
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

    private fun buildFullScheduleDays(
        lessons: List<FullScheduleLesson>,
        currentWeek: Int,
    ): List<FullScheduleDay> {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        fun parseDateSafe(value: String?): LocalDate? {
            if (value.isNullOrBlank()) return null
            return try {
                LocalDate.parse(value, formatter)
            } catch (e: DateTimeParseException) {
                e.printStackTrace()
                try {
                    LocalDate.parse(value)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        val startDates = lessons.mapNotNull {
            parseDateSafe(it.startLessonDate) ?: parseDateSafe(it.dateLesson)
        }
        val endDates = lessons.mapNotNull {
            parseDateSafe(it.endLessonDate) ?: parseDateSafe(it.dateLesson)
        }

        val globalStart = startDates.minOrNull() ?: return emptyList()
        val globalEnd = endDates.maxOrNull() ?: return emptyList()

        val today = LocalDate.now()
        val result = mutableListOf<FullScheduleDay>()

        var currentDate = globalStart
        while (!currentDate.isAfter(globalEnd)) {

            val dayOfWeek = currentDate.dayOfWeek

            if (dayOfWeek != DayOfWeek.SUNDAY) {

                val weekOffset = ChronoUnit.WEEKS.between(today, currentDate).toInt()

                val weekNumber = ((currentWeek - 1 + weekOffset) % 4 + 4) % 4 + 1

                val dayLessons = lessons.filter { lesson ->
                    val lessonDay = lesson.dayOfWeek ?: return@filter false
                    if (lessonDay != dayOfWeek) return@filter false
                    if (!lesson.weekNumber.contains(weekNumber)) return@filter false

                    val lessonStart =
                        parseDateSafe(lesson.startLessonDate) ?: parseDateSafe(lesson.dateLesson)
                    val lessonEnd =
                        parseDateSafe(lesson.endLessonDate) ?: parseDateSafe(lesson.dateLesson)

                    when {
                        lessonStart != null && lessonEnd != null ->
                            !currentDate.isBefore(lessonStart) && !currentDate.isAfter(lessonEnd)

                        lessonStart != null ->
                            !currentDate.isBefore(lessonStart)

                        lessonEnd != null ->
                            !currentDate.isAfter(lessonEnd)

                        else -> true
                    }
                }

                if (dayLessons.isNotEmpty()) {
                    result.add(
                        FullScheduleDay(
                            lessons = dayLessons,
                            date = currentDate
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli(),
                            week = weekNumber
                        )
                    )
                }
            }

            currentDate = currentDate.plusDays(1)
        }

        return result
    }

}


