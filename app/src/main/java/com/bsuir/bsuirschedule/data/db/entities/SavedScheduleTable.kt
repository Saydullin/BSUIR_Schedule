package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsuir.bsuirschedule.domain.models.SavedSchedule

@Entity
data class SavedScheduleTable (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @Embedded(prefix = "group_") val group: GroupTable,
    @Embedded(prefix = "employee_") val employee: EmployeeTable,
    @ColumnInfo val isGroup: Boolean,
    @ColumnInfo val lastUpdateTime: Long,
    @ColumnInfo val lastUpdateDate: String,
    @ColumnInfo val isUpdatedSuccessfully: Boolean,
    @ColumnInfo val isExistExams: Boolean
) {

    fun toSavedSchedule() = SavedSchedule(
        id = id,
        group = group.toGroup(),
        employee = employee.toEmployee(),
        isGroup = isGroup,
        lastUpdateTime = lastUpdateTime,
        lastUpdateDate = lastUpdateDate,
        isUpdatedSuccessfully = isUpdatedSuccessfully,
        isExistExams = isExistExams
    )

}


