package com.bsuir.bsuirschedule.data.repository

import com.bsuir.bsuirschedule.api.RetrofitBuilder
import com.bsuir.bsuirschedule.api.services.GetEmployeeItemsService
import com.bsuir.bsuirschedule.data.db.dao.EmployeeDao
import com.bsuir.bsuirschedule.domain.models.Employee
import com.bsuir.bsuirschedule.domain.repository.EmployeeItemsRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.StatusCode

class EmployeeItemsRepositoryImpl(override val employeeDao: EmployeeDao) : EmployeeItemsRepository {

    override suspend fun getEmployeeItemsAPI(): Resource<ArrayList<Employee>> {
        val employeeItems = RetrofitBuilder.getInstance().create(GetEmployeeItemsService::class.java)

        return try {
            val result = employeeItems.getEmployees()
            val data = result.body()
            return if (result.isSuccessful && data != null) {
                Resource.Success(data)
            } else {
                Resource.Error(
                    errorType = StatusCode.SERVER_ERROR,
                    message = result.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = StatusCode.CONNECTION_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun getEmployeeItems(): Resource<ArrayList<Employee>> {

        return try {
            val data = employeeDao.getEmployees().map { it.toEmployee() } as ArrayList<Employee>
            Resource.Success(data)
        } catch (e: Exception) {
            Resource.Error(
                errorType = StatusCode.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun filterByKeywordASC(s: String): Resource<ArrayList<Employee>> {

        return try {
            val data = employeeDao.filterByKeywordASC("%${s}%")
            val employeeList = data.map { it.toEmployee() } as ArrayList<Employee>
            Resource.Success(employeeList)
        } catch (e: Exception) {
            Resource.Error(
                errorType = StatusCode.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun filterByKeywordDESC(s: String): Resource<ArrayList<Employee>> {

        return try {
            val data = employeeDao.filterByKeywordDESC("%${s}%")
            val employeeList = data.map { it.toEmployee() } as ArrayList<Employee>
            Resource.Success(employeeList)
        } catch (e: Exception) {
            Resource.Error(
                errorType = StatusCode.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun saveEmployeeItem(employeeList: ArrayList<Employee>): Resource<Unit> {

        return try {
            employeeDao.saveEmployeeItem(employeeList.map { it.toEmployeeTable() })
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(
                errorType = StatusCode.DATABASE_ERROR,
                message = e.message
            )
        }
    }

}