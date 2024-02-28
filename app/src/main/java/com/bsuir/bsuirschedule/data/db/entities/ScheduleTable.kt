package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.*
import com.bsuir.bsuirschedule.data.db.converters.*
import com.bsuir.bsuirschedule.domain.models.*
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings

@Entity
data class ScheduleTable  (
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
    @TypeConverters(ScheduleDayListConverter::class) var schedules: ArrayList<ScheduleDay>,
    @TypeConverters(ScheduleDayListConverter::class) @ColumnInfo(defaultValue = "") var previousSchedules: ArrayList<ScheduleDay>?,
    @ColumnInfo val originalSchedule: ArrayList<ScheduleDay>?,
    @ColumnInfo val prevOriginalSchedule: ArrayList<ScheduleDay>?,
    @ColumnInfo val lastUpdateTime: Long,
    @ColumnInfo val lastOriginalUpdateTime: Long?,
    @ColumnInfo val currentPeriod: String?,
    @ColumnInfo val currentTerm: String?,
    @ColumnInfo val previousTerm: String?,
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
        subjectNow = null,
        schedules = schedules,
        previousSchedules = arrayListOf(),
        prevOriginalSchedule = prevOriginalSchedule ?: ArrayList(),
        originalSchedule = originalSchedule ?: ArrayList(),
        lastUpdateTime = lastUpdateTime,
        lastOriginalUpdateTime = lastOriginalUpdateTime ?: 0,
        settings = settings,
        currentPeriod = currentPeriod,
        currentTerm = currentTerm,
        previousTerm = previousTerm,
    )

}


