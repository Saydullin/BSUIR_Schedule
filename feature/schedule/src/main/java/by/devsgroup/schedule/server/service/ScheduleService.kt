package by.devsgroup.schedule.server.service

import by.devsgroup.schedule.server.model.ScheduleData
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleService {

    @GET("schedule")
    suspend fun getGroupSchedule(
        @Query("studentGroup") groupNumber: String
    ): ScheduleData?

}


