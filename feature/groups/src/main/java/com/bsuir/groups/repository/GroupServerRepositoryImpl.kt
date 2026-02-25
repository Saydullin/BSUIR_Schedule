package com.bsuir.groups.repository

import com.bsuir.domain.model.group.Group
import com.bsuir.domain.repository.groups.GroupServerRepository
import com.bsuir.groups.mapper.GroupDataToDomainMapper
import com.bsuir.groups.server.service.GroupsService
import com.bsuir.resource.Resource
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


