package by.devsgroup.database.departments.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.devsgroup.database.departments.entity.DepartmentEntity

@Dao
interface DepartmentDao {

    @Query("SELECT * FROM `department` ORDER BY name ASC")
    fun getAllDepartments(): List<DepartmentEntity>

    @Query("SELECT * FROM `department` WHERE abbrev = :abbrev ORDER BY name ASC LIMIT 1")
    fun getByAbbrev(abbrev: String): DepartmentEntity

    @Query("SELECT * FROM `department` WHERE name LIKE :name ORDER BY name ASC")
    fun getListByName(name: String): List<DepartmentEntity>

    @Query("SELECT * FROM `department` ORDER BY name ASC LIMIT :limit OFFSET :offset")
    fun getPagingDepartments(limit: Int, offset: Int): List<DepartmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(departments: List<DepartmentEntity>): List<Long>

    @Query("DELETE FROM `department`")
    fun clear(): Int

}