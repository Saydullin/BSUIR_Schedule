package by.devsgroup.database.employees.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import by.devsgroup.database.employees.entity.EmployeeEntity
import by.devsgroup.database.employees.relation.EmployeeWithDepartments

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM `employee` ORDER BY firstName ASC")
    fun getAllEmployees(): List<EmployeeEntity>

    @Transaction
    @Query("SELECT * FROM `employee` ORDER BY firstName ASC")
    fun getAllFullEmployees(): List<EmployeeWithDepartments>

    @Query("SELECT * FROM `employee` ORDER BY firstName ASC LIMIT :limit OFFSET :offset")
    fun getPagingEmployees(limit: Int, offset: Int): List<EmployeeWithDepartments>

    @Query("SELECT * FROM `employee` WHERE id = :id")
    fun getById(id: Int): EmployeeEntity?

    @Query("SELECT * FROM `employee` WHERE firstName = :name")
    fun getByName(name: String): EmployeeEntity?

    @Query("SELECT * FROM `employee` WHERE firstName LIKE :name")
    fun getListByName(name: String): List<EmployeeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveList(employees: List<EmployeeEntity>): List<Long>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(employee: EmployeeEntity): Long

    @Query("DELETE FROM `employee`")
    fun clear(): Int

}