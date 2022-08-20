package com.example.bsuirschedule.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bsuirschedule.domain.models.SavedSchedule

@Entity
data class SavedScheduleTable (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @Embedded(prefix = "group_") val group: GroupTable,
    @Embedded(prefix = "employee_") val employee: EmployeeTable,
    @ColumnInfo val isGroup: Boolean,
    @ColumnInfo val lastUpdateTime: Long,
    @ColumnInfo val isExistExams: Boolean
) {

    fun toSavedSchedule() = SavedSchedule(
        id = id,
        group = group.toGroup(),
        employee = employee.toEmployee(),
        isGroup = isGroup,
        lastUpdateTime = lastUpdateTime,
        isExistExams = isExistExams
    )

}


