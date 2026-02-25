package com.bsuir.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bsuir.database.departments.dao.DepartmentDao
import com.bsuir.database.departments.dao.EmployeeDepartmentDao
import com.bsuir.database.departments.entity.DepartmentEntity
import com.bsuir.database.departments.entity.EmployeeDepartmentEntity
import com.bsuir.database.employees.dao.EmployeeDao
import com.bsuir.database.employees.entity.EmployeeEntity
import com.bsuir.database.faculty.dao.FacultyDao
import com.bsuir.database.faculty.entity.FacultyEntity
import com.bsuir.database.groups.dao.GroupDao
import com.bsuir.database.groups.entity.GroupEntity
import com.bsuir.database.schedule.dao.ScheduleDao
import com.bsuir.database.schedule.dao.ScheduleDayDao
import com.bsuir.database.schedule.dao.ScheduleEmployeeDao
import com.bsuir.database.schedule.dao.ScheduleGroupDao
import com.bsuir.database.schedule.dao.ScheduleLessonDao
import com.bsuir.database.schedule.dao.ScheduleLessonEmployeeDao
import com.bsuir.database.schedule.dao.ScheduleLessonGroupDao
import com.bsuir.database.schedule.entity.ScheduleDayEntity
import com.bsuir.database.schedule.entity.ScheduleEmployeeEntity
import com.bsuir.database.schedule.entity.ScheduleEntity
import com.bsuir.database.schedule.entity.ScheduleGroupEntity
import com.bsuir.database.schedule.entity.ScheduleLessonEmployeeEntity
import com.bsuir.database.schedule.entity.ScheduleLessonEntity
import com.bsuir.database.schedule.entity.ScheduleLessonGroupEntity
import com.bsuir.database.specialty.dao.SpecialtyDao
import com.bsuir.database.specialty.entity.SpecialtyEducationFormEntity
import com.bsuir.database.specialty.entity.SpecialtyEntity
import com.bsuir.database.week.dao.WeekDao
import com.bsuir.database.week.entity.WeekEntity

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

    abstract fun getScheduleGroupDao(): ScheduleGroupDao

    abstract fun getScheduleLessonDao(): ScheduleLessonDao

    abstract fun getScheduleEmployeeDao(): ScheduleEmployeeDao

    abstract fun getEmployeeDepartmentDao(): EmployeeDepartmentDao

    abstract fun getScheduleLessonGroupDao(): ScheduleLessonGroupDao

    abstract fun getScheduleLessonEmployeeDao(): ScheduleLessonEmployeeDao

}


