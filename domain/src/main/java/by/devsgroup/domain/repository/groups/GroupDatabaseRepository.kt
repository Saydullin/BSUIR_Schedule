package by.devsgroup.domain.repository.groups

import by.devsgroup.domain.model.groups.Group
import by.devsgroup.resource.Resource

interface GroupDatabaseRepository {

    suspend fun getAllGroups(): Resource<List<Group>>

    suspend fun getGroupById(id: Int): Resource<Group?>

    suspend fun getGroupByName(name: String): Resource<Group?>

    suspend fun getGroupListByLikeName(name: String): Resource<List<Group>>

    suspend fun saveGroups(groups: List<Group>): Resource<Unit>

}