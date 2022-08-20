package com.example.bsuirschedule.domain.models

data class SubjectBreakTime (
    val hours: Int,
    val minutes: Int,
    val isExist: Boolean
) {

    companion object {
        val empty = SubjectBreakTime(
            hours = -1,
            minutes = -1,
            isExist = false
        )
    }

}


