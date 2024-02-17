package com.bsuir.bsuirschedule.domain.models.scheduleSettings

import com.bsuir.bsuirschedule.domain.models.ScheduleTerm

data class ScheduleSettingsTerm(
    var selectedTerm: ScheduleTerm,
) {

    companion object {
        val empty = ScheduleSettingsTerm(
            selectedTerm = ScheduleTerm.NOTHING
        )
        val schedule = ScheduleSettingsTerm(
            selectedTerm = ScheduleTerm.CURRENT_SCHEDULE
        )
    }

}


