package com.bsuir.bsuirschedule.domain.models

enum class SubjectHistoryStatus {
    DELETED,
    ADDED,
    NOTHING
}

data class ScheduleSubjectHistory (
    val id: Int,
    val scheduleSubject: ScheduleSubject,
    val status: SubjectHistoryStatus
) {

    companion object {
        val empty = ScheduleSubjectHistory(
            id = -1,
            scheduleSubject = ScheduleSubject.empty,
            status = SubjectHistoryStatus.ADDED
        )
    }

}