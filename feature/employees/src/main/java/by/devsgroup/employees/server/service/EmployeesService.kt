package by.devsgroup.employees.server.service

import by.devsgroup.employees.server.model.EmployeeData
import retrofit2.http.GET

interface EmployeesService {

    @GET("employees/all")
    suspend fun getAllEmployees(): List<EmployeeData>

}