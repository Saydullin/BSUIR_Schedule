package com.bsuir.database.employees.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.bsuir.database.departments.entity.EmployeeDepartmentEntity
import com.bsuir.database.employees.entity.EmployeeEntity

data class EmployeeWithDepartments(
    @Embedded val employee: EmployeeEntity,
    @Relation(
        parentColumn = "departmentKeyId",
        entityColumn = "employeeId",
    )
    val departments: List<EmployeeDepartmentEntity>
)


