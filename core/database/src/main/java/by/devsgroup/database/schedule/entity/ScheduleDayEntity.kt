package by.devsgroup.database.schedule.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedule_day",
    foreignKeys = [
        ForeignKey(
            entity = ScheduleEntity::class,
            parentColumns = ["scheduleId"],
            childColumns = ["scheduleId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index("scheduleId"),
        Index(
            value = ["dayId"],
            unique = true,
        ),
        Index(
            value = ["scheduleId", "dayId"],
            unique = true,
        )
    ]
)
data class ScheduleDayEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val scheduleId: Long,
    val dayId: String,
    val dateMillis: Long,
    val week: Int,
)


