package com.bsuir.bsuirschedule.domain.models

import android.content.Context
import com.bsuir.bsuirschedule.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class ScheduleUpdatedDay(
    val id: Int,
    val dateInMillis: Long,
    val actions: ArrayList<ScheduleUpdatedAction>
) {

    fun getDayOfWeek(): String {
        val pattern = SimpleDateFormat("EEEE")

        return pattern.format(dateInMillis).replaceFirstChar { it.uppercase() }
    }

    fun getDate(context: Context): String {
        val calendar = Calendar.getInstance(Locale("be", "BY"))
        val dayPattern = SimpleDateFormat("d MMMM")
        val todayDayNumber = calendar.get(Calendar.DATE)
        calendar.timeInMillis = dateInMillis

        return when (calendar.get(Calendar.DATE)) {
            todayDayNumber - 1 -> {
                context.getString(R.string.yesterday)
            }
            todayDayNumber -> {
                context.getString(R.string.today)
            }
            todayDayNumber + 1 -> {
                context.getString(R.string.tomorrow)
            }
            else -> {
                dayPattern.format(calendar.time)
            }
        }
    }

}


