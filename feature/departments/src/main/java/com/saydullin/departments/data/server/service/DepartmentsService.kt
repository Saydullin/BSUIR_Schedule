package com.saydullin.departments.data.server.service

import com.saydullin.departments.data.server.model.DepartmentData
import retrofit2.http.GET

interface DepartmentsService {

    @GET("departments")
    suspend fun getAllDepartments(): List<DepartmentData>

}