package com.bsuir.bsuirschedule.domain.repository

import com.bsuir.bsuirschedule.data.db.dao.GroupDao
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.utils.Resource

interface GroupItemsRepository {

    val groupDao: GroupDao

    suspend fun getGroupItemsAPI(): Resource<ArrayList<Group>>

    suspend fun getAllGroupItems(): Resource<ArrayList<Group>>

    suspend fun getSavedGroupItems(): Resource<ArrayList<Group>>

    suspend fun filterByKeywordASC(s: String): Resource<ArrayList<Group>>

    suspend fun filterByKeywordDESC(s: String): Resource<ArrayList<Group>>

    suspend fun saveGroupItemsList(groups: ArrayList<Group>): Resource<Unit>

}


