package com.bsuir.domain.repository.groups

import com.bsuir.domain.model.group.Group
import com.bsuir.resource.Resource

interface GroupServerRepository {

    suspend fun getAllGroups(): Resource<List<Group>>

}