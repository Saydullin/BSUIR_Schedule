package com.bsuir.database.schedule.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedule_lesson_employee",
    indices = [
        Index("lessonId"),
    ]
)
data class ScheduleLessonEmployeeEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val lessonId: String,
    val id: Long?,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val photoLink: String?,
    val degree: String?,
    val degreeAbbrev: String?,
    val rank: String?,
    val email: String?,
    val urlId: String?,
    val calendarId: String?,
    val chief: Boolean?,
)
