package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.*
import com.bsuir.bsuirschedule.data.db.converters.IntListConverter
import com.bsuir.bsuirschedule.data.db.converters.ScheduleDaysConverter
import com.bsuir.bsuirschedule.data.db.converters.ScheduleSubjectsConverter
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject

@Entity
data class ScheduleTable (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val startDate: String,
    @ColumnInfo val endDate: String,
    @ColumnInfo val startExamsDate: String,
    @ColumnInfo val endExamsDate: String,
    @ColumnInfo val lastUpdateTime: Long,
    @Embedded(prefix = "group_") val group: GroupTable,
    @Embedded(prefix = "employee_") val employee: EmployeeTable,
    @ColumnInfo val selectedSubgroup: Int,
    @TypeConverters(IntListConverter::class) val subgroups: List<Int>,
    @TypeConverters(ScheduleSubjectsConverter::class) val exams: ArrayList<ScheduleSubject>,
    @TypeConverters(ScheduleDaysConverter::class) val schedules: GroupScheduleWeekTable?
) {

    fun toGroupSchedule() = GroupSchedule(
        id = id,
        startDate = startDate,
        endDate = endDate,
        startExamsDate = startExamsDate,
        endExamsDate = endExamsDate,
        lastUpdateTime = lastUpdateTime,
        group = group.toGroup(),
        employee = employee.toEmployeeSubject(),
        subgroups = subgroups,
        schedules = schedules?.toGroupScheduleWeek(),
        exams = exams,
        selectedSubgroup = selectedSubgroup
    )

}


