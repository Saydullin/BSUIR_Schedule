package com.bsuir.bsuirschedule.domain.utils

import android.util.Log
import com.bsuir.bsuirschedule.domain.models.SubjectBreakTime
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class CalendarDate(startDate: String = "00.00.0000", private val weekNumber: Int = 1) {

    private val inputDate = SimpleDateFormat("dd.MM.yyyy").parse(startDate)
    private val calendar = CalendarInstance.get()
    private val timeZone = TimeZone.getTimeZone("Europe/Minsk")
    private var dayCounter = 0

    companion object {
        private val inputFormat = SimpleDateFormat("dd.MM.yyyy")
        val TODAY_DATE: String = inputFormat.format(Date().time)
        const val TODAY = "Today"
        const val YESTERDAY = "Yesterday"
        const val TOMORROW = "Tomorrow"
    }

    init {
        calendar.time = inputDate as Date
    }

    fun isEqualDate(endDate: String): Boolean {
        return endDate == inputFormat.format(calendar.time)
    }

    fun getFullDate(amount: Int): String {
        calendar.time = inputDate as Date
        calendar.add(Calendar.DATE, amount)
        val output = SimpleDateFormat("dd.MM.yyyy")

        return output.format(calendar.time)
    }

    fun getWeekNum(): Int {
        val currentCalendar = CalendarInstance.get() // today
        val differenceCalendar = CalendarInstance.get() // difference days
        val calendarCopy = CalendarInstance.get()
        calendarCopy.timeInMillis = calendar.timeInMillis
        currentCalendar.set(Calendar.DAY_OF_WEEK, 2)
        calendarCopy.set(Calendar.DAY_OF_WEEK, 2)
        val isPastDays = currentCalendar.get(Calendar.DAY_OF_YEAR) > calendarCopy.get(Calendar.DAY_OF_YEAR)

        val differenceTimeInMillis =
            if (isPastDays) {
                currentCalendar.timeInMillis - calendarCopy.timeInMillis
            } else {
                calendarCopy.timeInMillis - currentCalendar.timeInMillis
            }
        differenceCalendar.timeInMillis = differenceTimeInMillis
        val days = differenceCalendar.get(Calendar.DAY_OF_YEAR)

        var weekNum = (days / 7)
        if (isPastDays) {
            weekNum = 4 - ((weekNumber - weekNum) % 4).absoluteValue
        } else {
            weekNum = (weekNum + weekNumber) % 4
        }
        if (weekNum == 0) { weekNum = 4 }

        return weekNum
    }

    @Deprecated("Past weeks shows incorrectly. Instead use function getWeekNum()")
    fun getWeekNumber(): Int {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = CalendarInstance.get()
        val calendarDate = CalendarInstance.get()
        calendarDate.time = calendar.time
        currentDate.set(Calendar.DAY_OF_WEEK, 1) // current date
        calendarDate.set(Calendar.DAY_OF_WEEK, 1) // increment date
        var counter = weekNumber
        val amount = if (currentDate.time.time > calendarDate.time.time) -7 else 7

        while (dateFormat.format(currentDate.time) != dateFormat.format(calendarDate.time) && counter < 20) {
            currentDate.add(Calendar.DATE, amount)
            counter++
        }

        counter %= 4
        counter = if (counter == 0) 4 else counter

        return counter
    }

    fun minusDays(num: Int) {
        // FIXME Check if got number bigger than passed days from startDate (num < startDate)
        calendar.add(Calendar.DATE, num * -1)
    }

    fun isHoliday(date: Long): Boolean {
        val holidayCalendar = CalendarInstance.get()
        val yearsNow = holidayCalendar.get(Calendar.YEAR)
        holidayCalendar.timeInMillis = date
        val yearsOld = holidayCalendar.get(Calendar.YEAR)
        holidayCalendar.add(Calendar.YEAR, yearsNow - yearsOld)
        val holidayMonth = holidayCalendar.get(Calendar.MONTH)
        val month = calendar.get(Calendar.MONTH)
        val holidayDayOfMonth = holidayCalendar.get(Calendar.DAY_OF_MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        Log.e("sady", "${holidayCalendar.time} == ${calendar.time}")
        Log.e("sady", "isHoliday $holidayMonth == $month, " +
                "$holidayDayOfMonth == $dayOfMonth " +
                "${holidayMonth == month && holidayDayOfMonth == dayOfMonth}")

        return holidayMonth == month && holidayDayOfMonth == dayOfMonth
    }

    fun getDateInMillis(): Long {
        return calendar.timeInMillis
    }

    fun getDate(): String {
        val output = SimpleDateFormat("d MMMM")

        return output.format(calendar.time)
    }

    fun getDateStatus(): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val calendarNow = CalendarInstance.get()
        calendarNow.add(Calendar.DATE, -1)
        if (dateFormat.format(calendar.time) == dateFormat.format(calendarNow.time)) {
            return YESTERDAY
        }
        calendarNow.add(Calendar.DATE, 1)
        if (dateFormat.format(calendar.time) == dateFormat.format(calendarNow.time)) {
            return TODAY
        }
        calendarNow.add(Calendar.DATE, 1)
        if (dateFormat.format(calendar.time) == dateFormat.format(calendarNow.time)) {
            return TOMORROW
        }

        return getDate()
    }

    fun resetMillisTime(millisTime: Long): Long {
        val calMillis = CalendarInstance.get()
        calMillis.timeInMillis = millisTime
        calMillis.set(Calendar.HOUR, 0)
        calMillis.set(Calendar.HOUR_OF_DAY, 0)
        calMillis.set(Calendar.MINUTE, 0)
        calMillis.set(Calendar.SECOND, 0)
        calMillis.set(Calendar.MILLISECOND, 0)

        return calMillis.timeInMillis
    }

    fun getDateInMillis(datePattern: String): Long {
        val inputFormat = SimpleDateFormat("dd.MM.yyyy")

        return inputFormat.parse(datePattern)?.time ?: 0
    }

    fun getTimeInMillis(timePattern: String): Long {
        val inputFormat = SimpleDateFormat("dd.MM.yyyy")
        val timeFormat = SimpleDateFormat("dd.MM.yyyy kk:mm")
        timeFormat.timeZone = timeZone
        val resultFormat = timeFormat.parse("${inputFormat.format(calendar.time)} $timePattern")
            ?: return 0

        return resultFormat.time
    }

    fun incDate(amount: Int) {
        this.dayCounter += amount - this.dayCounter
        calendar.time = inputDate as Date
        calendar.add(Calendar.DATE, amount)
    }

    fun getIncDayCounter(): Int {
        val beginDateCalendar = CalendarInstance.get()
        beginDateCalendar.time = inputDate as Date
        beginDateCalendar.timeInMillis.minus(calendar.timeInMillis)
        return beginDateCalendar.get(Calendar.DATE)
    }

    fun getIncDate(amount: Int): String {
        calendar.time = inputDate as Date
        calendar.add(Calendar.DATE, amount)
        val output = SimpleDateFormat("d MMMM")

        return output.format(calendar.time)
    }

    fun isCurrentSubject(startTime: String, endTime: String): Boolean {
        val currCalendar = CalendarInstance.get()
        val inputFormat = SimpleDateFormat("dd.MM.yyyy")
        val timeFormat = SimpleDateFormat("dd.MM.yyyy kk:mm")
        val startFormat = timeFormat.parse("${inputFormat.format(calendar.time)} $startTime")
        val endFormat = timeFormat.parse("${inputFormat.format(calendar.time)} $endTime")

        if (startFormat == null || endFormat == null) return false

        return startFormat.time < currCalendar.time.time && endFormat.time > currCalendar.time.time
    }

    fun getWeekDayTitle(): String {
        val output = SimpleDateFormat("EEEE")

        return output.format(calendar.time)
    }

    fun isSunday(): Boolean {
        return calendar.get(Calendar.DAY_OF_WEEK) == calendar.get(Calendar.SUNDAY)
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


