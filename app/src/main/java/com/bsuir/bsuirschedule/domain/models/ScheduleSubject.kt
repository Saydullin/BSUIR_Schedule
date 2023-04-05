package com.bsuir.bsuirschedule.domain.models

import com.google.gson.annotations.SerializedName

data class ScheduleSubject (
    var id: Int? = 0,
    var subject: String?,
    var subjectFullName: String?,
    var lessonTypeAbbrev: String?,
    var employees: ArrayList<EmployeeSubject>?,
    var groups: ArrayList<Group>?,
    var edited: ScheduleSubjectEdit?,
    @SerializedName("studentGroups") var subjectGroups: ArrayList<GroupSubject>?,
    var nextTimeDaysLeft: Int?,
    var nextTimeSubject: ScheduleSubject?,
    var startMillis: Long = 0,
    var endMillis: Long = 0,
    val startLessonTime: String?,
    val endLessonTime: String?,
    var numSubgroup: Int? = 0,
    var note: String?,
    var breakTime: SubjectBreakTime?,
    val weekNumber: ArrayList<Int>?,
    var dayNumber: Int = 0,
    val dateLesson: String?,
    val startLessonDate: String?,
    val endLessonDate: String?,
    var isActual: Boolean? = false,
    var isIgnored: Boolean? = false,
    @SerializedName("auditories") var audience: ArrayList<String>?,
    ) {

    companion object {
        val empty = ScheduleSubject(
            id = 0,
            subject = "",
            subjectFullName = "",
            lessonTypeAbbrev = "",
            employees = ArrayList(),
            groups = ArrayList(),
            subjectGroups = ArrayList(),
            nextTimeDaysLeft = null,
            nextTimeSubject = null,
            startMillis = 0,
            endMillis = 0,
            startLessonTime = "",
            endLessonTime = "",
            numSubgroup = 0,
            note = "",
            breakTime = SubjectBreakTime.empty,
            weekNumber = ArrayList(),
            dayNumber = 0,
            dateLesson = "",
            startLessonDate = "",
            endLessonDate = "",
            isActual = false,
            isIgnored = false,
            audience = ArrayList(),
            edited = ScheduleSubjectEdit.empty
        )

        const val LESSON_TYPE_CONSULTATION = "Консультация"
        const val LESSON_TYPE_EXAM = "Экзамен"
        const val LESSON_TYPE_PRETEST = "Зачёт"
        const val LESSON_TYPE_LECTURE = "ЛК"
        const val LESSON_TYPE_PRACTISE = "ПЗ"
        const val LESSON_TYPE_LABORATORY = "ЛР"
    }

    fun getEditedOrShortTitle(): String {
        if (edited != null) {
            return edited!!.shortTitle
        }

        return subject ?: ""
    }

    fun getEditedOrFullTitle(): String {
        if (edited != null) {
            return edited!!.fullTitle
        }

        return subjectFullName ?: ""
    }

    fun getEditedOrGroups(): ArrayList<Group> {
        if (edited != null) {
            return edited!!.sourceItems.map { it.group } as ArrayList<Group>
        }

        return groups ?: arrayListOf()
    }

    fun getEditedOrEmployees(): ArrayList<EmployeeSubject> {
        if (edited != null) {
            return edited!!.sourceItems.map { it.employee.toEmployeeSubject() } as ArrayList<EmployeeSubject>
        }

        return employees ?: arrayListOf()
    }

    fun getEditedOrLessonType(): String {
        if (edited != null) {
            return edited!!.lessonType
        }

        return lessonTypeAbbrev ?: LESSON_TYPE_LECTURE
    }

    fun getEditedOrWeeks(): ArrayList<Int> {
        if (edited != null) {
            return edited!!.weeks
        }

        return weekNumber ?: arrayListOf()
    }

    fun getEditedOrWeekDay(): Int {
        if (edited != null) {
            return edited!!.weekDay
        }

        return dayNumber
    }

    fun getEditedOrNote(): String {
        if (edited != null) {
            return edited!!.note
        }

        return note ?: ""
    }

    fun getEditedOrAudienceInLine(): String {
        if (edited != null) {
            return edited!!.audience
        }

        return audience?.joinToString(", ") ?: ""
    }

    fun getEditedOrNumSubgroup(): Int {
        if (edited != null) {
            return edited!!.subgroup
        }

        return (numSubgroup ?: 0)
    }

    fun toSubjectHistory(status: SubjectHistoryStatus = SubjectHistoryStatus.NOTHING) = ScheduleSubjectHistory(
        id = id ?: -1,
        scheduleSubject = this,
        status = status
    )

}


