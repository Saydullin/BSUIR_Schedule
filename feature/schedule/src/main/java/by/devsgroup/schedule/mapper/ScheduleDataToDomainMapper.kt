package by.devsgroup.schedule.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.template.ScheduleLessonTemplate
import by.devsgroup.domain.model.schedule.template.ScheduleTemplate
import by.devsgroup.schedule.server.model.ScheduleData
import by.devsgroup.schedule.server.model.ScheduleLessonData
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ScheduleDataToDomainMapper @Inject constructor(
    private val scheduleLessonDataToDomainTemplateMapper: ScheduleLessonDataToDomainTemplateMapper,
    private val scheduleEmployeeDataToDomainMapper: ScheduleEmployeeDataToDomainMapper,
    private val scheduleGroupDataToDomainMapper: ScheduleGroupDataToDomainMapper,
): Mapper<ScheduleData, ScheduleTemplate> {

    override fun map(from: ScheduleData): ScheduleTemplate {
        val schedules = listOfNotNull(
            from.schedules?.monday?.let { mapLessons(it, DayOfWeek.MONDAY) },
            from.schedules?.tuesday?.let { mapLessons(it, DayOfWeek.TUESDAY) },
            from.schedules?.wednesday?.let { mapLessons(it, DayOfWeek.WEDNESDAY) },
            from.schedules?.thursday?.let { mapLessons(it, DayOfWeek.THURSDAY) },
            from.schedules?.friday?.let { mapLessons(it, DayOfWeek.FRIDAY) },
            from.schedules?.saturday?.let { mapLessons(it, DayOfWeek.SATURDAY) },
        ).flatten()

        val nextSchedules = listOfNotNull(
            from.nextSchedules?.monday?.let { mapLessons(it, DayOfWeek.MONDAY) },
            from.nextSchedules?.tuesday?.let { mapLessons(it, DayOfWeek.TUESDAY) },
            from.nextSchedules?.wednesday?.let { mapLessons(it, DayOfWeek.WEDNESDAY) },
            from.nextSchedules?.thursday?.let { mapLessons(it, DayOfWeek.THURSDAY) },
            from.nextSchedules?.friday?.let { mapLessons(it, DayOfWeek.FRIDAY) },
            from.nextSchedules?.saturday?.let { mapLessons(it, DayOfWeek.SATURDAY) },
        ).flatten()

        val exams = from.exams?.map { exam ->
            val dayOfWeek = exam.dateLesson?.let { getWeekFromDate(it) }

            scheduleLessonDataToDomainTemplateMapper.map(exam, dayOfWeek)
        }

        return ScheduleTemplate(
            startDate = from.startDate,
            endDate = from.endDate,
            startExamsDate = from.startExamsDate,
            endExamsDate = from.endExamsDate,
            employeeDto = from.employeeDto?.let { scheduleEmployeeDataToDomainMapper.map(it) },
            studentGroupDto = from.studentGroupDto?.let { scheduleGroupDataToDomainMapper.map(it) },
            schedules = schedules,
            nextSchedules = nextSchedules,
            currentTerm = from.currentTerm,
            nextTerm = from.nextTerm,
            exams = exams,
            currentPeriod = from.currentPeriod,
            partTimeOrRemote = from.isZaochOrDist,
        )
    }

    fun mapLessons(lessons: List<ScheduleLessonData>, dayOfWeek: DayOfWeek): List<ScheduleLessonTemplate> {
        return lessons.map { scheduleLessonDataToDomainTemplateMapper.map(it, dayOfWeek) }
    }

    fun getWeekFromDate(dateString: String): DayOfWeek? {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val date = LocalDate.parse(dateString, formatter)

            date.dayOfWeek
        } catch (e: Exception) {
            e.printStackTrace()

            null
        }
    }

}