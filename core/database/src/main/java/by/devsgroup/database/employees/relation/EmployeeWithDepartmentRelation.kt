package by.devsgroup.database.employees.relation

import androidx.room.Embedded
import androidx.room.Relation
import by.devsgroup.database.departments.entity.EmployeeDepartmentEntity
import by.devsgroup.database.employees.entity.EmployeeEntity

data class EmployeeWithDepartments(
    @Embedded val employee: EmployeeEntity,
    @Relation(
        parentColumn = "departmentKeyId",
        entityColumn = "employeeId",
    )
    val departments: List<EmployeeDepartmentEntity>
)


