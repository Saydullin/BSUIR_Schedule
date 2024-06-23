package com.bsuir.bsuirschedule.domain.manager.schedule.component

import com.bsuir.bsuirschedule.domain.models.ScheduleDay

class ScheduleSubgroup(
    private val scheduleDays: ArrayList<ScheduleDay>
) {

    fun execute(): List<Int> {
        val weeks = ArrayList<Int>()
        val subjects = scheduleDays.flatMap { it.schedule }

        weeks.add(0)

        subjects.forEach { subject ->
            weeks.add(subject.numSubgroup ?: 0)
        }

        return weeks.toSet().toList().sorted()
    }

}


