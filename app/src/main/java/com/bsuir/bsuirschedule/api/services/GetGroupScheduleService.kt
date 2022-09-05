package com.bsuir.bsuirschedule.api.services

import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetGroupScheduleService {

    @GET("schedule")
    suspend fun getGroupSchedule(@Query("studentGroup") studentGroup: String): Response<GroupSchedule>

    @GET("employees/schedule/{urlId}")
    suspend fun getEmployeeSchedule(@Path("urlId") urlId: String): Response<GroupSchedule>

}


