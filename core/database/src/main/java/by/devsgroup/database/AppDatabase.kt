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
import by.devsgroup.database.schedule.dao.ScheduleDao
import by.devsgroup.database.schedule.dao.ScheduleDayDao
import by.devsgroup.database.schedule.dao.ScheduleLessonDao
import by.devsgroup.database.schedule.dao.ScheduleLessonEmployeeDao
import by.devsgroup.database.schedule.dao.ScheduleLessonGroupDao
import by.devsgroup.database.schedule.entity.ScheduleDayEntity
import by.devsgroup.database.schedule.entity.ScheduleEmployeeEntity
import by.devsgroup.database.schedule.entity.ScheduleEntity
import by.devsgroup.database.schedule.entity.ScheduleGroupEntity
import by.devsgroup.database.schedule.entity.ScheduleLessonEmployeeEntity
import by.devsgroup.database.schedule.entity.ScheduleLessonEntity
import by.devsgroup.database.schedule.entity.ScheduleLessonGroupEntity
import by.devsgroup.database.specialty.dao.SpecialtyDao
import by.devsgroup.database.specialty.entity.SpecialtyEducationFormEntity
import by.devsgroup.database.specialty.entity.SpecialtyEntity
import by.devsgroup.database.week.dao.WeekDao
import by.devsgroup.database.week.entity.WeekEntity

@Database(
    version = 1,
    exportSchema = true,
    entities = [
        WeekEntity::class,
        GroupEntity::class,
        FacultyEntity::class,
        ScheduleEntity::class,
        EmployeeEntity::class,
        SpecialtyEntity::class,
        DepartmentEntity::class,
        ScheduleDayEntity::class,
        ScheduleGroupEntity::class,
        ScheduleLessonEntity::class,
        ScheduleEmployeeEntity::class,
        EmployeeDepartmentEntity::class,
        ScheduleLessonGroupEntity::class,
        ScheduleLessonEmployeeEntity::class,
        SpecialtyEducationFormEntity::class,
    ]
)
abstract class AppDatabase() : RoomDatabase() {

    abstract fun getWeekDao(): WeekDao

    abstract fun getGroupDao(): GroupDao

    abstract fun getFacultyDao(): FacultyDao

    abstract fun getEmployeeDao(): EmployeeDao

    abstract fun getScheduleDao(): ScheduleDao

    abstract fun getSpecialtyDao(): SpecialtyDao

    abstract fun getDepartmentDao(): DepartmentDao

    abstract fun getScheduleDayDao(): ScheduleDayDao

    abstract fun getScheduleLessonDao(): ScheduleLessonDao

    abstract fun getEmployeeDepartmentDao(): EmployeeDepartmentDao

    abstract fun getScheduleLessonGroupDao(): ScheduleLessonGroupDao

    abstract fun getScheduleLessonEmployeeDao(): ScheduleLessonEmployeeDao

}


