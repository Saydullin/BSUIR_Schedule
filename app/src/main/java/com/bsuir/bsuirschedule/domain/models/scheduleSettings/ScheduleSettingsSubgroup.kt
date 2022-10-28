package com.bsuir.bsuirschedule.domain.models.scheduleSettings

data class ScheduleSettingsSubgroup(
    var selectedNum: Int,
) {

    companion object {
        val empty = ScheduleSettingsSubgroup(
            selectedNum = 0 // Means all subgroups
        )
    }

}


