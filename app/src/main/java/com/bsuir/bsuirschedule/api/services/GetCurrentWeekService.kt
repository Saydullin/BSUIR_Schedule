package com.bsuir.bsuirschedule.api.services

import retrofit2.Response
import retrofit2.http.GET

interface GetCurrentWeekService {

    @GET("schedule/current-week")
    suspend fun getCurrentWeek(): Response<Int>

}