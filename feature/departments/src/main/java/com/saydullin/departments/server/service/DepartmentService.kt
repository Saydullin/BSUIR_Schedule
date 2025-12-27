package com.saydullin.departments.server.service

import com.saydullin.departments.server.model.DepartmentData
import retrofit2.http.GET

interface DepartmentService {

    @GET("departments")
    suspend fun getAllDepartments(): List<DepartmentData>

}