package by.devsgroup.database

import androidx.room.Database
import androidx.room.RoomDatabase
import by.devsgroup.database.departments.dao.DepartmentDao
import by.devsgroup.database.departments.dao.EmployeeDepartmentDao
import by.devsgroup.database.departments.entity.DepartmentEntity
import by.devsgroup.database.departments.entity.EmployeeDepartmentEntity
import by.devsgroup.database.employees.dao.EmployeeDao
import by.devsgroup.database.employees.entity.EmployeeEntity
import by.devsgroup.database.groups.dao.GroupDao
import by.devsgroup.database.groups.entity.GroupEntity

@Database(
    version = 1,
    exportSchema = true,
    entities = [
        GroupEntity::class,
        EmployeeEntity::class,
        DepartmentEntity::class,
        EmployeeDepartmentEntity::class,
    ]
)
abstract class AppDatabase() : RoomDatabase() {

    abstract fun getGroupDao(): GroupDao

    abstract fun getEmployeeDao(): EmployeeDao

    abstract fun getDepartmentDao(): DepartmentDao

    abstract fun getEmployeeDepartmentDao(): EmployeeDepartmentDao

}