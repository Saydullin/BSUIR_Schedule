package com.bsuir.domain.repository.schedule

import com.bsuir.domain.model.schedule.template.ScheduleTemplate
import com.bsuir.resource.Resource

interface ScheduleServerRepository {

    suspend fun getGroupSchedule(groupName: String): Resource<ScheduleTemplate>

    suspend fun getEmployeeSchedule(urlId: String): Resource<ScheduleTemplate>

}