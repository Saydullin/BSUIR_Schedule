package com.bsuir.domain.repository.groups

import com.bsuir.domain.model.group.Group
import com.bsuir.resource.Resource

interface GroupDatabaseRepository {

    suspend fun getAllGroups(): Resource<List<Group>>

    suspend fun getGroupById(id: Long): Resource<Group?>

    suspend fun getGroupByName(name: String): Resource<Group?>

    suspend fun getGroupListByLikeName(name: String): Resource<List<Group>>

    suspend fun saveGroups(groups: List<Group>): Resource<Unit>

    suspend fun clear(): Resource<Unit>

}