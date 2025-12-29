package by.devsgroup.database.schedule.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedule_lesson_entity"
)
data class ScheduleLessonEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long? = 0,
    val audiences: String,
    val endLessonTime: String,
    val startLessonTime: String,
    val lessonTypeAbbrev: String,
    val subject: String,
    val subjectFullName: String,
    val dateLesson: String?,
    val startLessonDate: String,
    val studentGroups: Long,
    val employees: Long,
    val endLessonDate: String,
    val announcement: String,
    val split: Boolean,
    val dateMillis: Long,
)


