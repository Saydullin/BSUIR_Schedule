package com.saydullin.departments.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "department"
)
data class DepartmentEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val id: Int?,
    val name: String?,
    val abbrev: String?,
    val urlId: String?,
)


