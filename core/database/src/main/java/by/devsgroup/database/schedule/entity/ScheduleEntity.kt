package by.devsgroup.database.schedule.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedule"
)
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val startDate: String?,
    val endDate: String?,
    val startExamsDate: String?,
    val endExamsDate: String?,
    val currentTerm: String?,
    val nextTerm: String?,
    val currentPeriod: String?,
    val isZaochOrDist: Boolean?,
    @Embedded(
        prefix = "employee_"
    )
    val employee: ScheduleEmployeeEntity,
    @Embedded(
        prefix = "group_"
    )
    val group: ScheduleGroupEntity,

)
