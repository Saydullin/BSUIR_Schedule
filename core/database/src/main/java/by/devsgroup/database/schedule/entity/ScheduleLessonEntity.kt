package by.devsgroup.database.schedule.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedule_lesson_entity",
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
        Index("lessonId"),
        Index(
            value = ["scheduleId", "lessonId"],
            unique = true
        )
    ]
)
data class ScheduleLessonEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val scheduleId: Long,
    val lessonId: String,
    val audiences: String?,
    val endLessonTime: String?,
    val startLessonTime: String?,
    val lessonTypeAbbrev: String?,
    val subject: String?,
    val subjectFullName: String?,
    val dateLesson: String?,
    val startLessonDate: String?,
    val endLessonDate: String?,
    val announcement: String?,
    val split: Boolean?,
)


