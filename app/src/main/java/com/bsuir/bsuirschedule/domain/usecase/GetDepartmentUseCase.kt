package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.Department
import com.bsuir.bsuirschedule.domain.repository.DepartmentRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class GetDepartmentUseCase(private val departmentRepository: DepartmentRepository) {

    suspend fun getDepartmentsAPI(): Resource<ArrayList<Department>> {
        return departmentRepository.getDepartmentsAPI()
    }

    suspend fun getDepartments(): Resource<ArrayList<Department>> {
        return departmentRepository.getDepartments()
    }

    suspend fun getDepartmentByAbbrev(abbrev: String): Resource<Department> {
        return departmentRepository.getDepartmentByAbbrev(abbrev)
    }

    suspend fun saveDepartments(departmentsList: ArrayList<Department>): Resource<Unit> {
        return departmentRepository.saveDepartments(departmentsList)
    }

}