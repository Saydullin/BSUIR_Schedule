package by.devsgroup.database.week.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "week"
)
data class WeekEntity(
    @PrimaryKey(autoGenerate = false) val tableId: Long = 1,
    val week: Int,
    val createdAt: Long,
)
