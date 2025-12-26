package by.devsgroup.groups.usecase

import by.devsgroup.domain.repository.groups.GroupDatabaseRepository
import by.devsgroup.domain.repository.groups.GroupServerRepository
import by.devsgroup.resource.Resource
import javax.inject.Inject

class GetAndSaveAllGroupsUseCase @Inject constructor(
    private val groupDatabaseRepository: GroupDatabaseRepository,
    private val groupServerRepository: GroupServerRepository,
) {

    suspend fun execute(): Resource<Unit> {
        return Resource.tryWithSuspend {
            val groups = groupServerRepository.getAllGroups()
                .getOrThrow()

            groupDatabaseRepository.clear()
            groupDatabaseRepository.saveGroups(groups)
        }
    }

}