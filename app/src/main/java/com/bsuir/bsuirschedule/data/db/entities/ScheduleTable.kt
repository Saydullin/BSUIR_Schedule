package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.*
import com.bsuir.bsuirschedule.data.db.converters.IntListConverter
import com.bsuir.bsuirschedule.data.db.converters.ScheduleDayListConverter
import com.bsuir.bsuirschedule.data.db.converters.ScheduleSubjectsConverter
import com.bsuir.bsuirschedule.domain.models.*

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
    @TypeConverters(ScheduleSubjectsConverter::class) var exams: ArrayList<ScheduleSubject>,
    @TypeConverters(ScheduleDayListConverter::class) var examsSchedule: ArrayList<ScheduleDay>,
    @TypeConverters(ScheduleDayListConverter::class) var schedules: ArrayList<ScheduleDay>,
    @ColumnInfo val lastUpdateTime: Long,
    @ColumnInfo var selectedSubgroup: Int = 0, // 0 - non selected, show all subgroups
    @Embedded(prefix = "settings_") val settings: ScheduleSettingsTable
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
        lastUpdateTime = lastUpdateTime,
        selectedSubgroup = selectedSubgroup, // 0 - non selected, show all subgroups
        settings = settings.toScheduleSettings()
    )

}


