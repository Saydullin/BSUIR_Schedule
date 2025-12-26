package by.devsgroup.employees.data.server.service

import by.devsgroup.employees.data.server.model.EmployeeData
import retrofit2.http.GET

interface EmployeesService {

    @GET("employees/all")
    suspend fun getAllEmployees(): List<EmployeeData>

}