package by.devsgroup.domain.repository.groups

import by.devsgroup.domain.model.groups.Group
import by.devsgroup.resource.Resource

interface GroupServerRepository {

    suspend fun getAllGroups(): Resource<List<Group>>

}