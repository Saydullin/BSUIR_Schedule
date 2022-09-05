package com.bsuir.bsuirschedule.api.services

import com.bsuir.bsuirschedule.domain.models.Group
import retrofit2.Response
import retrofit2.http.GET

interface GetGroupItemsService {

    @GET("student-groups")
    suspend fun getGroupItems(): Response<ArrayList<Group>>

}


