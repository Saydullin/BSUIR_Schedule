package by.devsgroup.employees.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.devsgroup.employees.data.db.entity.EmployeeEntity

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM `employee` ORDER BY firstName ASC")
    fun getAllEmployees(): List<EmployeeEntity>

    @Query("SELECT * FROM `employee` ORDER BY firstName ASC LIMIT :limit OFFSET :offset")
    fun getPagingEmployees(limit: Int, offset: Int): List<EmployeeEntity>

    @Query("SELECT * FROM `employee` WHERE id = :id")
    fun getById(id: Int): EmployeeEntity?

    @Query("SELECT * FROM `employee` WHERE firstName = :name")
    fun getByName(name: String): EmployeeEntity?

    @Query("SELECT * FROM `employee` WHERE firstName LIKE :name")
    fun getListByName(name: String): List<EmployeeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(employees: List<EmployeeEntity>): List<Long>

    @Query("DELETE FROM `employee`")
    fun clear(): Int

}