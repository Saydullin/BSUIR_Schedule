package com.bsuir.bsuirschedule.domain.models.scheduleSettings

data class ScheduleSettings (
    val id: Int,
    var subgroup: ScheduleSettingsSubgroup,
    var schedule: ScheduleSettingsSchedule,
    var exams: ScheduleSettingsExams,
    val alarmSettings: ScheduleSettingsAlarm
) {

    companion object {
        val empty = ScheduleSettings(
            id = -1,
            subgroup = ScheduleSettingsSubgroup.empty,
            schedule = ScheduleSettingsSchedule.empty,
            exams = ScheduleSettingsExams.empty,
            alarmSettings = ScheduleSettingsAlarm.empty
        )
        val fullSchedule = ScheduleSettings(
            id = -1,
            subgroup = ScheduleSettingsSubgroup.empty,
            schedule = ScheduleSettingsSchedule.fullSchedule,
            exams = ScheduleSettingsExams.fullSchedule,
            alarmSettings = ScheduleSettingsAlarm.empty
        )
    }

}


