package com.bsuir.bsuirschedule.domain.models

data class ScheduleSubjectEdit (
    val shortTitle: String,
    val fullTitle: String,
    val audience: String,
    val startTime: String,
    val endTime: String,
    val lessonType: String,
    val weeks: ArrayList<Int>,
    val sourceItems: ArrayList<SavedSchedule>,
    val note: String,
    val subgroup: Int,
) {

    companion object {
        val empty = ScheduleSubjectEdit(
            shortTitle = "",
            fullTitle = "",
            audience = "",
            startTime = "",
            weeks = arrayListOf(),
            sourceItems = arrayListOf(),
            lessonType = "",
            endTime = "",
            note = "",
            subgroup = 0,
        )
    }

}


