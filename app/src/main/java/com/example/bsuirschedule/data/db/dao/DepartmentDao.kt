package com.example.bsuirschedule.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bsuirschedule.data.db.entities.DepartmentTable

@Dao
interface DepartmentDao {

    @Query("SELECT * FROM DepartmentTable")
    fun getDepartments(): List<DepartmentTable>

    @Query("SELECT * FROM DepartmentTable WHERE abbr=:abbr")
    fun getDepartmentByAbbr(abbr: String): DepartmentTable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDepartments(departments: List<DepartmentTable>)

}


