package com.bsuir.schedule.server.service

import com.bsuir.schedule.server.model.ScheduleData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleService {

    @GET("schedule")
    suspend fun getGroupSchedule(
        @Query("studentGroup") groupNumber: String
    ): ScheduleData?

    @GET("employees/schedule/{urlId}")
    suspend fun getEmployeeSchedule(
        @Path("urlId") urlId : String
    ): ScheduleData?

}


