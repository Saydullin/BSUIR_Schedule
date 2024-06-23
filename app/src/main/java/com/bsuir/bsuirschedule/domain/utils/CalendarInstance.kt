package com.bsuir.bsuirschedule.domain.utils

import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class CalendarInstance {

    companion object {

        fun get(): Calendar {
            val calendarDate = Calendar.getInstance(Locale("ru", "BY"))
            val timeZone = TimeZone.getTimeZone("Europe/Minsk")
            calendarDate.timeZone = timeZone

            return calendarDate
        }

    }

}


