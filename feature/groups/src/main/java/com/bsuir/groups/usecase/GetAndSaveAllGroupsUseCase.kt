package com.bsuir.groups.usecase

import com.bsuir.domain.repository.groups.GroupDatabaseRepository
import com.bsuir.domain.repository.groups.GroupServerRepository
import com.bsuir.resource.Resource
import javax.inject.Inject

class GetAndSaveAllGroupsUseCase @Inject constructor(
    private val groupDatabaseRepository: GroupDatabaseRepository,
    private val groupServerRepository: GroupServerRepository,
) {

    suspend fun execute(): Resource<Unit> {
        return Resource.tryWithSuspend {
            val groups = groupServerRepository
                .getAllGroups()
                .getOrThrow()

            groupDatabaseRepository.saveGroups(groups)
        }
    }

}