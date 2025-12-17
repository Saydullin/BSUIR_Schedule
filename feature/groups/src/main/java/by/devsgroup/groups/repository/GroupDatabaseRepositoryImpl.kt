package by.devsgroup.groups.repository

import by.devsgroup.domain.model.groups.Group
import by.devsgroup.domain.repository.groups.GroupDatabaseRepository
import by.devsgroup.groups.data.db.dao.GroupDao
import by.devsgroup.groups.data.mapper.GroupEntityToDomainMapper
import by.devsgroup.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupDatabaseRepositoryImpl @Inject constructor(
    private val groupDao: GroupDao,
    private val groupEntityToDomainMapper: GroupEntityToDomainMapper,
): GroupDatabaseRepository {

    override suspend fun getAllGroups(): Resource<List<Group>> {
        return Resource.tryWithSuspend {
            val groupEntityList = withContext(Dispatchers.IO) { groupDao.getAllGroups() }

            groupEntityList.map { groupEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun getGroupById(id: Int): Resource<Group?> {
        return Resource.tryWithSuspend {
            val groupEntity = withContext(Dispatchers.IO) { groupDao.getById(id) }

            groupEntity?.let { groupEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun getGroupByName(name: String): Resource<Group?> {
        return Resource.tryWithSuspend {
            val groupEntity = withContext(Dispatchers.IO) { groupDao.getByName(name) }

            groupEntity?.let { groupEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun getGroupListByLikeName(name: String): Resource<List<Group>> {
        return Resource.tryWithSuspend {
            val groupEntityList = withContext(Dispatchers.IO) { groupDao.getListByName("%$name%") }

            groupEntityList.map { groupEntityToDomainMapper.map(it) }
        }
    }

}


