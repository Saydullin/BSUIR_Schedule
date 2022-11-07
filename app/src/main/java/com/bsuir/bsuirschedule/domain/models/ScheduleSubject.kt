package com.bsuir.bsuirschedule.domain.models

import com.google.gson.annotations.SerializedName

data class ScheduleSubject (
    var id: Int? = 0,
    val subject: String?,
    val subjectFullName: String?,
    var lessonTypeAbbrev: String?,
    var employees: ArrayList<EmployeeSubject>?,
    var groups: ArrayList<Group>?,
    @SerializedName("studentGroups") val subjectGroups: ArrayList<GroupSubject>?,
    var nextTimeDaysLeft: Int?,
    var nextTimeSubject: ScheduleSubject?,
    var startMillis: Long = 0,
    var endMillis: Long = 0,
    val startLessonTime: String?,
    val endLessonTime: String?,
    val numSubgroup: Int? = 0,
    val note: String?,
    var breakTime: SubjectBreakTime?,
    val weekNumber: ArrayList<Int>?,
    var dayNumber: Int = 0,
    val dateLesson: String?,
    val startLessonDate: String?,
    val endLessonDate: String?,
    var isActual: Boolean? = false,
    var isIgnored: Boolean? = false,
    @SerializedName("auditories") val audience: ArrayList<String>?
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
            audience = ArrayList()
        )

        const val CONSULTATION = "УЗк"
        const val EXAM = "УЛк"
        const val LECTURE = "ЛК"
        const val PRACTISE = "ПЗ"
        const val LABORATORY = "ЛР"
    }

    fun getNumSubgroupStr() = (numSubgroup ?: 0).toString()

    fun getAudienceInLine() = audience?.joinToString(", ") ?: ""

//    fun toGroupScheduleSubjectTable() = GroupScheduleSubjectTable(
//        subject = subject ?: "",
//        subjectFullName = subjectFullName ?: "",
//        lessonTypeAbbrev = lessonTypeAbbrev ?: "",
//        employees = employees?.map { it.toEmployeeTable() } ?: ArrayList(),
//        groups = groups?.map { it.toGroupTable() } ?: ArrayList(),
//        groupSubjects = subjectGroups?.map { it.toGroupSubjectTable() } ?: ArrayList(),
//        startMillis = startMillis,
//        endMillis = endMillis,
//        nextTimeDaysLeft = nextTimeDaysLeft ?: -1,
//        startLessonTime = startLessonTime ?: "",
//        endLessonTime = endLessonTime ?: "",
//        numSubgroup = numSubgroup ?: 0,
//        note = note ?: "",
//        breakMinutes = breakTime ?: SubjectBreakTime.empty,
//        weekNumber = weekNumber ?: ArrayList(),
//        dayNumber = dayNumber,
//        dateLesson = dateLesson ?: "",
//        startLessonDate = startLessonDate ?: "",
//        endLessonDate = endLessonDate ?: "",
//        isActual = isActual ?: false,
//        isIgnored = isIgnored ?: false,
//        audience = audience ?: ArrayList(),
//    )

}


