package com.example.bsuirschedule.api.services

import com.example.bsuirschedule.domain.models.Group
import retrofit2.Response
import retrofit2.http.GET

interface GetGroupItemsService {

    @GET("student-groups")
    suspend fun getGroupItems(): Response<ArrayList<Group>>

}


