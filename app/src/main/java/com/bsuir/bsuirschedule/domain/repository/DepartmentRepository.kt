package com.bsuir.bsuirschedule.domain.repository

import com.bsuir.bsuirschedule.data.db.dao.DepartmentDao
import com.bsuir.bsuirschedule.domain.models.Department
import com.bsuir.bsuirschedule.domain.utils.Resource

interface DepartmentRepository {

    val departmentDao: DepartmentDao

    suspend fun getDepartmentsAPI(): Resource<ArrayList<Department>>

    suspend fun getDepartments(): Resource<ArrayList<Department>>

    suspend fun getDepartmentByAbbrev(abbrev: String): Resource<Department>

    suspend fun saveDepartments(departmentList: ArrayList<Department>): Resource<Unit>

}


