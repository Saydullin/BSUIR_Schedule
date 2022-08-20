package com.example.bsuirschedule.domain.repository

import com.example.bsuirschedule.data.db.dao.DepartmentDao
import com.example.bsuirschedule.domain.models.Department
import com.example.bsuirschedule.domain.utils.Resource

interface DepartmentRepository {

    val departmentDao: DepartmentDao

    suspend fun getDepartmentsAPI(): Resource<ArrayList<Department>>

    suspend fun getDepartments(): Resource<ArrayList<Department>>

    suspend fun getDepartmentByAbbrev(abbrev: String): Resource<Department>

    suspend fun saveDepartments(departmentList: ArrayList<Department>): Resource<Unit>

}


