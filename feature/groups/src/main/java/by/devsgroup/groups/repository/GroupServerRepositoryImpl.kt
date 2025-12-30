package by.devsgroup.groups.repository

import by.devsgroup.domain.model.group.Group
import by.devsgroup.domain.repository.groups.GroupServerRepository
import by.devsgroup.groups.mapper.GroupDataToDomainMapper
import by.devsgroup.groups.server.service.GroupsService
import by.devsgroup.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupServerRepositoryImpl @Inject constructor(
    private val groupsService: GroupsService,
    private val groupDataToDomainMapper: GroupDataToDomainMapper,
): GroupServerRepository {

    override suspend fun getAllGroups(): Resource<List<Group>> {
        return Resource.tryWithSuspend {
            val groups = withContext(Dispatchers.IO) {
                groupsService.getAllStudentGroups()
            }

            groups.map { groupDataToDomainMapper.map(it) }
        }
    }

}


