package by.devsgroup.database

import androidx.room.Database
import androidx.room.RoomDatabase
import by.devsgroup.database.departments.dao.DepartmentDao
import by.devsgroup.database.departments.dao.EmployeeDepartmentDao
import by.devsgroup.database.departments.entity.DepartmentEntity
import by.devsgroup.database.departments.entity.EmployeeDepartmentEntity
import by.devsgroup.database.employees.dao.EmployeeDao
import by.devsgroup.database.employees.entity.EmployeeEntity
import by.devsgroup.database.faculty.dao.FacultyDao
import by.devsgroup.database.faculty.entity.FacultyEntity
import by.devsgroup.database.groups.dao.GroupDao
import by.devsgroup.database.groups.entity.GroupEntity
import by.devsgroup.database.specialty.dao.SpecialtyDao
import by.devsgroup.database.specialty.entity.SpecialtyEducationFormEntity
import by.devsgroup.database.specialty.entity.SpecialtyEntity

@Database(
    version = 1,
    exportSchema = true,
    entities = [
        GroupEntity::class,
        FacultyEntity::class,
        EmployeeEntity::class,
        SpecialtyEntity::class,
        DepartmentEntity::class,
        EmployeeDepartmentEntity::class,
        SpecialtyEducationFormEntity::class,
    ]
)
abstract class AppDatabase() : RoomDatabase() {

    abstract fun getGroupDao(): GroupDao

    abstract fun getFacultyDao(): FacultyDao

    abstract fun getEmployeeDao(): EmployeeDao

    abstract fun getSpecialtyDao(): SpecialtyDao

    abstract fun getDepartmentDao(): DepartmentDao

    abstract fun getEmployeeDepartmentDao(): EmployeeDepartmentDao

}