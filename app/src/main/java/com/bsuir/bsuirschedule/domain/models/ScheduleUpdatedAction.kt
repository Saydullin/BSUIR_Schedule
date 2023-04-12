package com.bsuir.bsuirschedule.domain.models

enum class ScheduleUpdatedActionsList {
    CHANGED_AUDIENCE,
    CHANGED_SUBGROUP,
    CHANGED_TEACHER,
    CHANGED_GROUP,
    CHANGED_LESSON_TYPE,
    CHANGED_WEEKS,
    CHANGED_NOTE,
    ADDED_SUBJECT,
    DELETED_SUBJECT,
}

data class ScheduleUpdatedAction(
    val id: Int,
    val action: ScheduleUpdatedActionsList,
    val itemsCount: Int,
    val items: List<ScheduleSubject>
)


