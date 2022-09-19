package com.bsuir.bsuirschedule.domain.utils

import android.util.Log
import com.bsuir.bsuirschedule.domain.models.SubjectBreakTime
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class CalendarDate(startDate: String = "00.00.0000", private val weekNumber: Int = 1) {

    private val inputDate = SimpleDateFormat("dd.MM.yyyy").parse(startDate)
    private val calendar = Calendar.getInstance()

    companion object {
        private val inputFormat = SimpleDateFormat("dd.MM.yyyy")
        val TODAY_DATE: String = inputFormat.format(Date().time)
        const val TODAY = "Today"
        const val TOMORROW = "Tomorrow"
    }

    init {
        calendar.time = inputDate as Date
    }

    fun getCalendar() = calendar

    fun getFullDate(amount: Int): String {
        calendar.time = inputDate as Date
        calendar.add(Calendar.DATE, amount)
        val output = SimpleDateFormat("dd.MM.yyyy")

        return output.format(calendar.time)
    }

    fun getWeekNumber(): Int {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val nowDate = Calendar.getInstance()
        val calendarDate = Calendar.getInstance()
        calendarDate.time = calendar.time
        nowDate.set(Calendar.DAY_OF_WEEK, 1) // now date
        calendarDate.set(Calendar.DAY_OF_WEEK, 1) // increment date
        var counter = weekNumber
        val amount = if (nowDate.time.time > calendarDate.time.time) -7 else 7

        while (dateFormat.format(nowDate.time) != dateFormat.format(calendarDate.time) && counter < 20) {
            nowDate.add(Calendar.DATE, amount)
            counter++
        }

        counter %= 4
        counter = if (counter == 0) 4 else counter

        return counter
    }

    fun getDate(): String {
        val output = SimpleDateFormat("d MMMM")

        return output.format(calendar.time)
    }

    fun getDateStatus(): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val calendarNow = Calendar.getInstance()
        if (dateFormat.format(calendar.time) == dateFormat.format(calendarNow.time)) {
            return TODAY
        }
        calendarNow.add(Calendar.DATE, 1)
        if (dateFormat.format(calendar.time) == dateFormat.format(calendarNow.time)) {
            return TOMORROW
        }
        return getDate()
    }

    fun getIncDate(amount: Int): String {
        calendar.time = inputDate as Date
        calendar.add(Calendar.DATE, amount)
        val output = SimpleDateFormat("d MMMM")

        return output.format(calendar.time)
    }

    fun getWeekDayName(): String {
        val output = SimpleDateFormat("EEEE")

        return output.format(calendar.time)
    }

    fun getWeekDayNumber(): Int {
        return calendar.get(Calendar.DAY_OF_WEEK) - 1
    }

    fun getSubjectBreakTime(fromPattern: String?, reducerPattern: String?): SubjectBreakTime {
        if (fromPattern.isNullOrEmpty() || reducerPattern.isNullOrEmpty()) {
            return SubjectBreakTime(
                hours = 0,
                minutes = 0,
                isExist = true
            )
        }
        val from = SimpleDateFormat("HH:mm").parse(fromPattern)
        val reducer = SimpleDateFormat("HH:mm").parse(reducerPattern)
        val difference = (from?.time ?: 0) - (reducer?.time ?: 0)
        val seconds = difference / 1000
        val hours = seconds / 60 / 60
        val minutes = seconds / 60 - hours * 60

        return SubjectBreakTime(
            hours = if (hours.toInt() > 0) hours.toInt() else 0,
            minutes = if (minutes.toInt() > 0) minutes.toInt() else 0,
            isExist = true
        )
    }

}


