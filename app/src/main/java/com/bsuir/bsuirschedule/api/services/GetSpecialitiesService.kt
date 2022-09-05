package com.bsuir.bsuirschedule.api.services

import com.bsuir.bsuirschedule.domain.models.Speciality
import retrofit2.Response
import retrofit2.http.GET

interface GetSpecialitiesService {

    @GET("specialities")
    suspend fun getSpecialities(): Response<ArrayList<Speciality>>

}


