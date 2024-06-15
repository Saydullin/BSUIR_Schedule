package com.bsuir.bsuirschedule.domain.manager.schedule.component

import com.bsuir.bsuirschedule.domain.models.ScheduleDay

class ScheduleSubgroup(
    private val scheduleDays: ArrayList<ScheduleDay>
) {

    fun execute(): List<Int> {
        val amount = ArrayList<Int>()

        amount.add(0)

        if (scheduleDays.isNotEmpty()) {
            scheduleDays.forEach { day ->
                day.schedule.forEach { subject ->
                    amount.add(subject.numSubgroup ?: 0)
                }
            }
        }

        return amount.toSet().toList().sorted()
    }

}


