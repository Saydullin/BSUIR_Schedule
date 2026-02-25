package com.bsuir.groups.server.service

import com.bsuir.groups.server.model.GroupData
import retrofit2.http.GET

interface GroupsService {

    @GET("student-groups")
    suspend fun getAllStudentGroups(): List<GroupData>

}