package com.bsuir.groups.repository

import com.bsuir.domain.model.group.Group
import com.bsuir.groups.mapper.GroupDataToDomainMapper
import com.bsuir.groups.server.model.GroupData
import com.bsuir.groups.server.service.GroupsService
import com.bsuir.resource.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GroupServerRepositoryImplTest {

    private val groupsService: GroupsService = mockk()
    private val mapper: GroupDataToDomainMapper = mockk()

    private lateinit var repository: GroupServerRepositoryImpl

    @Before
    fun setUp() {
        repository = GroupServerRepositoryImpl(groupsService, mapper)
    }

    @Test
    fun `getAllGroups returns mapped groups`() = runTest {
        val groupData1 = mockk<GroupData>()
        val groupData2 = mockk<GroupData>()

        val group1 = mockk<Group>()
        val group2 = mockk<Group>()

        val groupDataList = listOf(
            groupData1,
            groupData2
        )

        coEvery { groupsService.getAllStudentGroups() } returns groupDataList

        val result = repository.getAllGroups()

        assert(result is Resource.Success)

        val data = (result as Resource.Success).data
    }

}


