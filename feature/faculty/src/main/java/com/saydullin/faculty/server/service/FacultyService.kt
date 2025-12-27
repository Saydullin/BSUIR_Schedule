package com.saydullin.faculty.server.service

import com.saydullin.faculty.server.model.FacultyData
import retrofit2.http.GET

interface FacultyService {

    @GET("faculties")
    suspend fun getAllFaculties(): List<FacultyData>

}