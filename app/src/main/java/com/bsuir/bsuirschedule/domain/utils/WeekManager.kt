package com.bsuir.bsuirschedule.domain.utils

import android.util.Log
import com.bsuir.bsuirschedule.domain.models.CurrentWeek
import java.text.SimpleDateFormat
import java.util.*

class WeekManager {

    companion object {
        const val MAX_WEEKS = 4
        const val WEEK_DAYS = 7
        const val WEEK_LIMIT = 50
    }

    fun getCurrentWeek(initWeek: CurrentWeek): Int {
        val passedWeeks = calculatePassedWeeks(initWeek)
        val currentWeek = (passedWeeks + initWeek.week) % MAX_WEEKS

        return if (currentWeek == 0) 4 else currentWeek
    }

    fun isWeekPassed(initWeek: CurrentWeek): Boolean {
        val initDate = Calendar.getInstance()
        val currDate = Calendar.getInstance()
        initDate.time.time = initWeek.time
        initDate.add(Calendar.DATE, 1)

        return initDate.time.time < currDate.time.time
    }

    private fun calculatePassedWeeks(initWeek: CurrentWeek): Int {
        val inputFormat = SimpleDateFormat("dd.MM.yyyy")
        val initDate = Calendar.getInstance()
        val currDate = Calendar.getInstance()
        initDate.timeInMillis = initWeek.time
        initDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        currDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val currDateFormat = inputFormat.format(currDate.time)
        var weekAmount = 0

        while (inputFormat.format(initDate.time) != currDateFormat && weekAmount < WEEK_LIMIT) {
            weekAmount++
            initDate.add(Calendar.DATE, WEEK_DAYS)
        }

        return weekAmount
    }

}


