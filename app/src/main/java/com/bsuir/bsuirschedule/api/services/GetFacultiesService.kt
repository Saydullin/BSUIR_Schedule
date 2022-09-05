package com.bsuir.bsuirschedule.api.services

import com.bsuir.bsuirschedule.domain.models.Faculty
import retrofit2.Response
import retrofit2.http.GET

interface GetFacultiesService {

    @GET("faculties")
    suspend fun getFaculties(): Response<ArrayList<Faculty>>

}


