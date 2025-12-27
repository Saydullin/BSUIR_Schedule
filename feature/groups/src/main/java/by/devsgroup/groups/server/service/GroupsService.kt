package by.devsgroup.groups.server.service

import by.devsgroup.groups.server.model.GroupData
import retrofit2.http.GET

interface GroupsService {

    @GET("student-groups")
    suspend fun getAllStudentGroups(): List<GroupData>

}