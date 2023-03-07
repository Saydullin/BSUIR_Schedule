package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.*
import com.bsuir.bsuirschedule.data.db.converters.*
import com.bsuir.bsuirschedule.domain.models.*
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings

@Entity
data class ScheduleTable (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val startDate: String,
    @ColumnInfo val endDate: String,
    @ColumnInfo val startExamsDate: String,
    @ColumnInfo val endExamsDate: String,
    @Embedded(prefix = "group_") val group: GroupTable,
    @Embedded(prefix = "employee_") val employee: EmployeeTable,
    @TypeConverters(IntListConverter::class) val subgroups: List<Int>,
    @ColumnInfo val isGroup: Boolean?,
    @TypeConverters(ScheduleSubjectsListConverter::class) var exams: ArrayList<ScheduleSubject>,
    @TypeConverters(ScheduleDayListConverter::class) var examsSchedule: ArrayList<ScheduleDay>,
    @ColumnInfo val updateHistorySchedule: ArrayList<ScheduleDayUpdateHistory>?,
    @TypeConverters(ScheduleDayListConverter::class) var schedules: ArrayList<ScheduleDay>,
    @ColumnInfo var normalSchedules: ArrayList<ScheduleDay>?,
    @ColumnInfo val lastUpdateTime: Long,
    @ColumnInfo val lastUpdateDate: String,
    @TypeConverters(ScheduleSettingsConverter::class) val settings: ScheduleSettings
) {

    fun toSchedule() = Schedule(
        id = id,
        startDate = startDate,
        endDate = endDate,
        startExamsDate = startExamsDate,
        endExamsDate = endExamsDate,
        group = group.toGroup(),
        employee = employee.toEmployeeSubject(),
        subgroups = subgroups,
        isGroup = isGroup,
        exams = exams,
        examsSchedule = examsSchedule,
        updateHistorySchedule = updateHistorySchedule ?: ArrayList(),
        subjectNow = null,
        schedules = schedules,
        originalSchedule = normalSchedules ?: ArrayList(),
        lastUpdateTime = lastUpdateTime,
        lastUpdateDate = lastUpdateDate,
        settings = settings
    )

}


