package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.*
import com.bsuir.bsuirschedule.domain.models.*

@Entity
data class GroupScheduleSubjectTable(
    @PrimaryKey(autoGenerate = true) var id: Int = -1,
    @ColumnInfo val subject: String,
    @ColumnInfo val subjectFullName: String,
    @ColumnInfo val lessonTypeAbbrev: String,
    @Embedded val employees: List<EmployeeTable>,
    @Embedded val groups: List<GroupTable>,
    @ColumnInfo val nextTimeDaysLeft: Int,
    @Embedded val groupSubjects: List<GroupSubjectTable>,
    @ColumnInfo val startMillis: Long,
    @ColumnInfo val endMillis: Long,
    @ColumnInfo val startLessonTime: String,
    @ColumnInfo val endLessonTime: String,
    @ColumnInfo val numSubgroup: Int,
    @ColumnInfo val note: String,
    @Embedded var breakMinutes: SubjectBreakTime,
    @ColumnInfo val weekNumber: ArrayList<Int>,
    @ColumnInfo val audience: ArrayList<String>,
    @ColumnInfo val dayNumber: Int,
    @ColumnInfo val dateLesson: String,
    @ColumnInfo val startLessonDate: String,
    @ColumnInfo val endLessonDate: String,
    @ColumnInfo val isActual: Boolean,
    @ColumnInfo val isIgnored: Boolean
) {

    fun toScheduleSubject() = ScheduleSubject(
        id = id,
        subject = subject,
        subjectFullName = subjectFullName,
        lessonTypeAbbrev = lessonTypeAbbrev,
        employees = employees.map { it.toEmployeeSubject() } as ArrayList<EmployeeSubject>,
        groups = groups.map { it.toGroup() } as ArrayList<Group>,
        subjectGroups = groupSubjects.map { it.toGroupSubject() } as ArrayList<GroupSubject>,
        nextTimeDaysLeft = nextTimeDaysLeft,
        nextTimeSubject = null,
        startMillis = startMillis,
        endMillis = endMillis,
        startLessonTime = startLessonTime,
        endLessonTime = endLessonTime,
        numSubgroup = numSubgroup,
        note = note,
        breakTime = breakMinutes,
        weekNumber = weekNumber,
        dayNumber = dayNumber,
        dateLesson = dateLesson,
        startLessonDate = startLessonDate,
        endLessonDate = endLessonDate,
        isActual = isActual,
        isIgnored = isIgnored,
        audience = audience
    )

}


