package com.example.bsuirschedule.domain.repository

import com.example.bsuirschedule.data.db.dao.EmployeeDao
import com.example.bsuirschedule.domain.models.Employee
import com.example.bsuirschedule.domain.utils.Resource

interface EmployeeItemsRepository {

    val employeeDao: EmployeeDao

    suspend fun getEmployeeItemsAPI(): Resource<ArrayList<Employee>>

    suspend fun getEmployeeItems(): Resource<ArrayList<Employee>>

    suspend fun filterByKeywordASC(s: String): Resource<ArrayList<Employee>>

    suspend fun filterByKeywordDESC(s: String): Resource<ArrayList<Employee>>

    suspend fun saveEmployeeItem(employeeList: ArrayList<Employee>): Resource<Unit>

    suspend fun deleteEmployeeItem(employee: Employee): Resource<Unit>

    suspend fun getEmployeeItemByName(employeeName: String): Resource<ArrayList<Employee>>

}