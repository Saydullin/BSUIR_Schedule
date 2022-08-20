package com.example.bsuirschedule.domain.utils

import com.example.bsuirschedule.domain.models.SubjectBreakTime
import java.text.SimpleDateFormat
import java.util.*

class CalendarDate(startDate: String = "00.00.0000", endDate: String = "00.00.0000") {

    private val inputDate = SimpleDateFormat("dd.MM.yyyy").parse(startDate)
    private val calendar = Calendar.getInstance()

    init {
        calendar.time = inputDate as Date
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

    fun getSubjectBreakTime(fromPattern: String, reducerPattern: String): SubjectBreakTime {
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


