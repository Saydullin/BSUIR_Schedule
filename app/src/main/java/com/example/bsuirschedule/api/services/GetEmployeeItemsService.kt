package com.example.bsuirschedule.api.services

import com.example.bsuirschedule.domain.models.Employee
import retrofit2.Response
import retrofit2.http.GET

interface GetEmployeeItemsService {

    @GET("employees/all")
    suspend fun getEmployees(): Response<ArrayList<Employee>>

}