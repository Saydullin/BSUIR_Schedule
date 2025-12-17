package by.devsgroup.groups.data.server.service

import by.devsgroup.groups.data.server.model.GroupData
import retrofit2.http.GET

interface GroupsService {

    @GET("student-groups")
    suspend fun getAllStudentGroups(): List<GroupData>

}