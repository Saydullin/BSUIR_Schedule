package com.bsuir.bsuirschedule.domain.models

data class ScheduleSubjectEdit (
    val shortTitle: String = "",
    val fullTitle: String = "",
    val audience: String = "",
    val note: String = "",
    val subgroup: Int = 0,
) {

    companion object {
        val empty = ScheduleSubjectEdit(
            shortTitle = "",
            fullTitle = "",
            audience = "",
            note = "",
            subgroup = 0,
        )
    }

}


