package com.example.bsuirschedule.api.services

import com.example.bsuirschedule.domain.models.Department
import retrofit2.Response
import retrofit2.http.GET

interface GetDepartmentsService {

    @GET("departments")
    suspend fun getDepartments(): Response<ArrayList<Department>>

}