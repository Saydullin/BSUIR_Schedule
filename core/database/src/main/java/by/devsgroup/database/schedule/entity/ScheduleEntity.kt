package by.devsgroup.database.schedule.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedule",
    indices = [
        Index(
            value = ["scheduleId"],
            unique = true,
        )
    ]
)
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val scheduleId: Long,
    val startDate: String?,
    val endDate: String?,
    val startExamsDate: String?,
    val endExamsDate: String?,
    val currentTerm: String?,
    val nextTerm: String?,
    val currentPeriod: String?,
    val partTimeOrRemote: Boolean?,
    @Embedded(
        prefix = "employee_"
    )
    val employee: ScheduleEmployeeEntity?,
    @Embedded(
        prefix = "group_"
    )
    val group: ScheduleGroupEntity?,
)
