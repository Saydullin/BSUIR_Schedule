package com.bsuir.bsuirschedule.data.repository

import androidx.core.text.isDigitsOnly
import com.bsuir.bsuirschedule.api.RetrofitBuilder
import com.bsuir.bsuirschedule.api.services.GetGroupItemsService
import com.bsuir.bsuirschedule.data.db.dao.GroupDao
import com.bsuir.bsuirschedule.data.db.entities.GroupTable
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.repository.GroupItemsRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class GroupItemsRepositoryImpl(override val groupDao: GroupDao) : GroupItemsRepository {

    override suspend fun getGroupItemsAPI(): Resource<ArrayList<Group>> {
        val groupItemsService = RetrofitBuilder.getInstance().create(GetGroupItemsService::class.java)

        return try {
            val result = groupItemsService.getGroupItems()
            val data = result.body()
            return if (result.isSuccessful && data != null) {
                Resource.Success(data)
            } else {
                Resource.Error(
                    errorType = Resource.SERVER_ERROR,
                    message = result.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.CONNECTION_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun getAllGroupItems(): Resource<ArrayList<Group>> {
        return try {
            val data = groupDao.getAllGroups()
            val groupList = data.map { it.toGroup() } as ArrayList<Group>
            Resource.Success(groupList)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun getSavedGroupItems(): Resource<ArrayList<Group>> {
        return try {
            val data = groupDao.getSavedGroups().map { it.toGroup() } as ArrayList<Group>
            Resource.Success(data)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun filterByKeywordASC(s: String): Resource<ArrayList<Group>> {
        return try {
            val data: List<GroupTable> = if (s.length == 1 && s.isDigitsOnly()) {
                val res = groupDao.filterByCourseASC(s.toInt())
                if (res.isEmpty()) {
                    groupDao.filterByKeywordASC("%${s}%")
                } else {
                    res
                }
            } else {
                groupDao.filterByKeywordASC("%${s}%")
            }
            val groupList = data.map { it.toGroup() } as ArrayList<Group>
            Resource.Success(groupList)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun filterByKeywordDESC(s: String): Resource<ArrayList<Group>> {
        return try {
            val data: List<GroupTable> = if (s.length == 1 && s.isDigitsOnly()) {
                val res = groupDao.filterByCourseDESC(s.toInt())
                if (res.isEmpty()) {
                    groupDao.filterByKeywordDESC("%${s}%")
                } else {
                    res
                }
            } else {
                groupDao.filterByKeywordDESC("%${s}%")
            }
            val groupList = data.map { it.toGroup() } as ArrayList<Group>
            Resource.Success(groupList)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun saveGroupItemsList(groups: ArrayList<Group>): Resource<Unit> {
        return try {
            val groupList = groups.map { it.toGroupTable() }
            groupDao.saveAllGroups(groupList)
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

}