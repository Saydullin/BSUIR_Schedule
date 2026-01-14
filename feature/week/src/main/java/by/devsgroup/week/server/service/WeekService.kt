package by.devsgroup.week.server.service

import retrofit2.http.GET

interface WeekService {

    @GET("schedule/current-week")
    suspend fun getCurrentWeek(): Int

}