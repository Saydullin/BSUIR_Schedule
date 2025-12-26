package by.devsgroup.database

import androidx.room.Database
import androidx.room.RoomDatabase
import by.devsgroup.employees.data.db.dao.EmployeeDao
import by.devsgroup.employees.data.db.entity.EmployeeEntity
import by.devsgroup.groups.data.db.dao.GroupDao
import by.devsgroup.groups.data.db.entity.GroupEntity
import com.saydullin.departments.data.db.dao.DepartmentDao
import com.saydullin.departments.data.db.entity.DepartmentEntity

@Database(
    version = 1,
    exportSchema = true,
    entities = [
        GroupEntity::class,
        EmployeeEntity::class,
        DepartmentEntity::class,
    ]
)
abstract class AppDatabase() : RoomDatabase() {

    abstract fun getGroupDao(): GroupDao

    abstract fun getEmployeeDao(): EmployeeDao

    abstract fun getDepartmentDao(): DepartmentDao

}