package com.bsuir.bsuirschedule.data.repository

import com.bsuir.bsuirschedule.api.RetrofitBuilder
import com.bsuir.bsuirschedule.api.services.GetDepartmentsService
import com.bsuir.bsuirschedule.data.db.dao.DepartmentDao
import com.bsuir.bsuirschedule.domain.models.Department
import com.bsuir.bsuirschedule.domain.repository.DepartmentRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.StatusCode

class DepartmentRepositoryImpl(override val departmentDao: DepartmentDao) : DepartmentRepository {

    override suspend fun getDepartmentsAPI(): Resource<ArrayList<Department>> {
        val departmentsService = RetrofitBuilder.getInstance().create(GetDepartmentsService::class.java)

        return try {
            val result = departmentsService.getDepartments()
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

    override suspend fun getDepartments(): Resource<ArrayList<Department>> {

        return try {
            val result = departmentDao.getDepartments()
            val departmentsList = result.map { it.toDepartment() } as ArrayList<Department>
            Resource.Success(departmentsList)
        } catch (e: Exception) {
            Resource.Error(
                errorType = StatusCode.DATABASE_ERROR,
                message = e.message,
            )
        }
    }

    override suspend fun getDepartmentByAbbrev(abbrev: String): Resource<Department> {

        return try {
            val result = departmentDao.getDepartmentByAbbr(abbrev)
            Resource.Success(result.toDepartment())
        } catch (e: Exception) {
            Resource.Error(
                errorType = StatusCode.DATABASE_ERROR,
                message = e.message,
            )
        }
    }

    override suspend fun saveDepartments(departmentList: ArrayList<Department>): Resource<Unit> {

        return try {
            val departmentsList = departmentList.map { it.toDepartmentTable() }
            departmentDao.saveDepartments(departmentsList)
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(
                errorType = StatusCode.DATABASE_ERROR,
                message = e.message,
            )
        }
    }

}


