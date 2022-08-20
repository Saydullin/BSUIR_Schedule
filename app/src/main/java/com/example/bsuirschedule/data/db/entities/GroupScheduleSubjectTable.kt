package com.example.bsuirschedule.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bsuirschedule.domain.models.*

@Entity
data class GroupScheduleSubjectTable(
    @PrimaryKey(autoGenerate = true) var id: Int = -1,
    @ColumnInfo val subject: String,
    @ColumnInfo val subjectFullName: String,
    @ColumnInfo val lessonTypeAbbrev: String,
    @Embedded val employees: List<EmployeeTable>,
    @Embedded val groups: List<GroupTable>,
    @Embedded val groupSubjects: List<GroupSubjectTable>,
    @ColumnInfo val startLessonTime: String,
    @ColumnInfo val endLessonTime: String,
    @ColumnInfo val numSubgroup: Int,
    @ColumnInfo val note: String,
    @Embedded var breakMinutes: SubjectBreakTime,
    @ColumnInfo val weekNumber: ArrayList<Int>,
    @ColumnInfo val audience: ArrayList<String>,
    @ColumnInfo val dateLesson: String,
    @ColumnInfo val startLessonDate: String,
    @ColumnInfo val endLessonDate: String
) {

    fun toGroupScheduleSubject() = ScheduleSubject(
        subject = subject,
        subjectFullName = subjectFullName,
        lessonTypeAbbrev = lessonTypeAbbrev,
        employees = employees.map { it.toEmployeeSubject() } as ArrayList<EmployeeSubject>,
        groups = groups.map { it.toGroup() } as ArrayList<Group>,
        subjectGroups = groupSubjects.map { it.toGroupSubject() } as ArrayList<GroupSubject>,
        startLessonTime = startLessonTime,
        endLessonTime = endLessonTime,
        numSubgroup = numSubgroup,
        note = note,
        breakTime = breakMinutes,
        weekNumber = weekNumber,
        dateLesson = dateLesson,
        startLessonDate = startLessonDate,
        endLessonDate = endLessonDate,
        audience = audience
    )

}


