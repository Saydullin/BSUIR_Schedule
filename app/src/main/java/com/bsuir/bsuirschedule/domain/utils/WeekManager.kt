package com.bsuir.bsuirschedule.domain.utils

import com.bsuir.bsuirschedule.domain.models.CurrentWeek
import java.text.SimpleDateFormat
import java.util.*

class WeekManager {

    companion object {
        const val MAX_WEEKS = 4
        const val WEEK_DAYS = 7
    }

    fun getCurrentWeek(initWeek: CurrentWeek): Int {
        val passedWeeks = calculatePassedWeeks(initWeek)
        val currentWeek = (passedWeeks + initWeek.week) % MAX_WEEKS

        return if (currentWeek == 0) 1 else currentWeek
    }

    fun isWeekPassed(initWeek: CurrentWeek): Boolean {
        val initDate = Calendar.getInstance()
        val currDate = Calendar.getInstance()
        initDate.time.time = initWeek.time
        initDate.set(Calendar.DAY_OF_WEEK, 1)
        initDate.add(Calendar.DATE, 7)

        return initDate.time.time >= currDate.time.time
    }

    private fun calculatePassedWeeks(initWeek: CurrentWeek): Int {
        val inputFormat = SimpleDateFormat("dd.MM.yyyy")
        val initDate = Calendar.getInstance()
        val currDate = Calendar.getInstance()
        initDate.time.time = initWeek.time
        initDate.set(Calendar.DAY_OF_WEEK, 1)
        currDate.set(Calendar.DAY_OF_WEEK, 1)
        val initFormatDate = inputFormat.format(initDate.time)
        var weekAmount = 0

        while (initFormatDate != inputFormat.format(currDate.time)) {
            weekAmount++
            initDate.add(Calendar.DATE, WEEK_DAYS)
        }

        return weekAmount
    }

}


