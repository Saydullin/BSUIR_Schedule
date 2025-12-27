package by.devsgroup.database.departments.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.devsgroup.database.departments.entity.EmployeeDepartmentEntity

@Dao
interface EmployeeDepartmentDao {

    @Query("SELECT * FROM `employee_department` WHERE employeeId = :employeeId")
    fun getByEmployeeId(employeeId: String): EmployeeDepartmentEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(department: EmployeeDepartmentEntity): Long

    @Query("DELETE FROM `department`")
    fun clear(): Int

}