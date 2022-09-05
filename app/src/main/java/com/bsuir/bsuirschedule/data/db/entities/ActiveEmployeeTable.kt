package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsuir.bsuirschedule.domain.models.Employee
import com.bsuir.bsuirschedule.domain.models.GroupSchedule

@Entity
data class ActiveEmployeeTable (
    @PrimaryKey val id: Int,
    @ColumnInfo val employee: Employee,
    @ColumnInfo val employeeSchedule: GroupSchedule
)


