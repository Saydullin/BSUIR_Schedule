package com.bsuir.employees.server.service

import com.bsuir.employees.server.model.EmployeeData
import retrofit2.http.GET

interface EmployeesService {

    @GET("employees/all")
    suspend fun getAllEmployees(): List<EmployeeData>

}