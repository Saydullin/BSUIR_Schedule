package com.saydullin.specialty.server.service

import com.saydullin.specialty.server.model.SpecialtyData
import retrofit2.http.GET

interface SpecialtyService {

    @GET("specialities")
    suspend fun getAllSpecialities(): List<SpecialtyData>

}