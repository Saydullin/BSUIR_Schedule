package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.GroupScheduleSubjectTable
import com.google.gson.annotations.SerializedName

data class ScheduleSubject (
    val subject: String?,
    val subjectFullName: String?,
    var lessonTypeAbbrev: String?,
    var employees: ArrayList<EmployeeSubject>?,
    var groups: ArrayList<Group>?,
    @SerializedName("studentGroups") val subjectGroups: ArrayList<GroupSubject>?,
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
    @SerializedName("auditories") val audience: ArrayList<String>?
) {

    companion object {
        val empty = ScheduleSubject(
            "",
            "",
            "",
            ArrayList(),
            ArrayList(),
            ArrayList(),
            "",
            "",
            0,
            "",
            SubjectBreakTime.empty,
            ArrayList(),
            0,
            "",
            "",
            "",
            ArrayList()
        )

        const val CONSULTATION = "УЗк"
        const val EXAM = "УЛк"
        const val LECTURE = "ЛК"
        const val PRACTISE = "ПЗ"
        const val LABORATORY = "ЛР"
    }

    fun getNumSubgroupStr() = (numSubgroup ?: 0).toString()

    fun getAudienceInLine() = audience?.joinToString(", ") ?: ""

    fun toGroupScheduleSubjectTable() = GroupScheduleSubjectTable(
        subject = subject ?: "",
        subjectFullName = subjectFullName ?: "",
        lessonTypeAbbrev = lessonTypeAbbrev ?: "",
        employees = employees?.map { it.toEmployeeTable() } ?: ArrayList(),
        groups = groups?.map { it.toGroupTable() } ?: ArrayList(),
        groupSubjects = subjectGroups?.map { it.toGroupSubjectTable() } ?: ArrayList(),
        startLessonTime = startLessonTime ?: "",
        endLessonTime = endLessonTime ?: "",
        numSubgroup = numSubgroup ?: 0,
        note = note ?: "",
        breakMinutes = breakTime ?: SubjectBreakTime.empty,
        weekNumber = weekNumber ?: ArrayList(),
        dayNumber = dayNumber,
        dateLesson = dateLesson ?: "",
        startLessonDate = startLessonDate ?: "",
        endLessonDate = endLessonDate ?: "",
        audience = audience ?: ArrayList(),
    )

}


