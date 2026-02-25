package com.bsuir.database.schedule.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedule_lesson_group",
    indices = [
        Index("lessonId"),
    ]
)
data class ScheduleLessonGroupEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val lessonId: String,
    val specialityName: String?,
    val specialityCode: String?,
    val numberOfStudents: Int?,
    val name: String?,
    val educationDegree: Int?,
)


