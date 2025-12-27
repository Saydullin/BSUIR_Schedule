package by.devsgroup.database.departments.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import by.devsgroup.database.employees.entity.EmployeeEntity

@Entity(
    tableName = "employee_department",
    indices = [
        Index(
            value = ["employeeId"],
            unique = true
        ),
        Index(
            value = ["abbrev"],
            unique = false
        )
    ],
    foreignKeys = [
        ForeignKey(
            entity = EmployeeEntity::class,
            parentColumns = ["departmentKeyId"],
            childColumns = ["employeeId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class EmployeeDepartmentEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val id: Int?,
    val name: String?,
    val abbrev: String?,
    val urlId: String?,
    val employeeId: String,
)


