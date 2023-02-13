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
    @SerializedName("studentGroups") val subjectGroups: ArrayList<GroupSubject>?,
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
        const val LESSON_TYPE_DELETED = "subject_deleted"
        const val LESSON_TYPE_ADDED = "subject_added"
    }

    fun getShortTitle(): String {
        if (edited != null) {
            return edited!!.shortTitle
        }

        return subject ?: ""
    }

    fun getFullTitle(): String {
        if (edited != null) {
            return edited!!.fullTitle
        }

        return subjectFullName ?: ""
    }

    fun getSubjectNote(): String {
        if (edited != null) {
            return edited!!.note
        }

        return note ?: ""
    }

    fun getAudienceInLine(): String {
        if (edited != null) {
            return edited!!.audience
        }

        return audience?.joinToString(", ") ?: ""
    }

    fun getNumSubgroup(): Int {
        if (edited != null) {
            return edited!!.subgroup
        }
        return (numSubgroup ?: 0)
    }

}


