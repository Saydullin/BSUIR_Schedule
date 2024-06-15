package com.bsuir.bsuirschedule.domain.manager.schedule.component

import android.util.Log
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.StatusCode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleFromGroupSchedule(
    private val groupSchedule: GroupSchedule,
) {

    fun execute(): Resource<Schedule> {
        return try {
            val previousSchedule = transformToPreviousSchedule()
            val schedule = transformToSchedule()
            Log.e("sady", "previousSchedule.previousSchedules ${previousSchedule.previousSchedules}")

            schedule.previousSchedules = previousSchedule.previousSchedules
            Resource.Success(schedule)
        } catch (e: Exception) {
            Resource.Error(
                statusCode = StatusCode.DATA_ERROR
            )
        }
    }

    private fun transformToSchedule(): Schedule {
        val weekDays = arrayListOf(
            initScheduleDay(groupSchedule.schedules?.monday ?: ArrayList(), 1, Calendar.MONDAY),
            initScheduleDay(groupSchedule.schedules?.tuesday ?: ArrayList(), 2, Calendar.TUESDAY),
            initScheduleDay(groupSchedule.schedules?.wednesday ?: ArrayList(), 3, Calendar.WEDNESDAY),
            initScheduleDay(groupSchedule.schedules?.thursday ?: ArrayList(), 4, Calendar.THURSDAY),
            initScheduleDay(groupSchedule.schedules?.friday ?: ArrayList(), 5, Calendar.FRIDAY),
            initScheduleDay(groupSchedule.schedules?.saturday ?: ArrayList(), 6, Calendar.SATURDAY),
            initScheduleDay(groupSchedule.schedules?.sunday ?: ArrayList(), 0, Calendar.SUNDAY),
        )

        val schedule = groupSchedule.toSchedule()

        if (schedule.isGroup()) {
            schedule.id = groupSchedule.group?.id ?: -1
        } else {
            schedule.id = groupSchedule.employee?.id ?: -1
        }

        schedule.schedules = weekDays

        val subjectTypes = listOf(
            ScheduleSubject.LESSON_TYPE_LECTURE,
            ScheduleSubject.LESSON_TYPE_PRACTISE,
            ScheduleSubject.LESSON_TYPE_LABORATORY,
        )
        filterSubjectsByType(schedule.schedules, subjectTypes)

        return schedule
    }

    private fun transformToPreviousSchedule(): Schedule {
        val weekDays = arrayListOf(
            initScheduleDay(groupSchedule.previousSchedules?.monday ?: ArrayList(), 1, Calendar.MONDAY),
            initScheduleDay(groupSchedule.previousSchedules?.tuesday ?: ArrayList(), 2, Calendar.TUESDAY),
            initScheduleDay(groupSchedule.previousSchedules?.wednesday ?: ArrayList(), 3, Calendar.WEDNESDAY),
            initScheduleDay(groupSchedule.previousSchedules?.thursday ?: ArrayList(), 4, Calendar.THURSDAY),
            initScheduleDay(groupSchedule.previousSchedules?.friday ?: ArrayList(), 5, Calendar.FRIDAY),
            initScheduleDay(groupSchedule.previousSchedules?.saturday ?: ArrayList(), 6, Calendar.SATURDAY),
            initScheduleDay(groupSchedule.previousSchedules?.sunday ?: ArrayList(), 0, Calendar.SUNDAY),
        )

        val schedule = groupSchedule.toSchedule()

        if (schedule.isGroup()) {
            schedule.id = groupSchedule.group?.id ?: -1
        } else {
            schedule.id = groupSchedule.employee?.id ?: -1
        }

        schedule.previousSchedules = weekDays

        val subjectTypes = listOf(
            ScheduleSubject.LESSON_TYPE_LECTURE,
            ScheduleSubject.LESSON_TYPE_PRACTISE,
            ScheduleSubject.LESSON_TYPE_LABORATORY,
        )
        filterSubjectsByType(schedule.previousSchedules, subjectTypes)

        return schedule
    }

    private fun filterSubjectsByType(scheduleDaysList: ArrayList<ScheduleDay>, typesList: List<String>) {
        scheduleDaysList.map { scheduleDay ->
            scheduleDay.schedule = scheduleDay.schedule.filter { typesList.contains(it.lessonTypeAbbrev) } as ArrayList<ScheduleSubject>
        }
    }

    private fun initScheduleDay(subjects: ArrayList<ScheduleSubject>, dayNumber: Int, calendarDayOfWeek: Int): ScheduleDay {
        val calendar = Calendar.getInstance(Locale("ru", "BY"))
        calendar.set(Calendar.DAY_OF_WEEK, calendarDayOfWeek)
        val output = SimpleDateFormat("EEEE")

        return ScheduleDay(
            id = -1,
            date = "",
            dateInMillis = 0,
            weekDayTitle = output.format(calendar.time),
            weekDayNumber = dayNumber,
            weekNumber = -1,
            schedule = subjects
        )
    }

}


