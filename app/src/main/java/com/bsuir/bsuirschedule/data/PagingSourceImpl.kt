package com.bsuir.bsuirschedule.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bsuir.bsuirschedule.domain.usecase.GroupScheduleUseCase
import com.bsuir.bsuirschedule.domain.utils.Resource

class PagingSourceImpl(
    private val groupScheduleUseCase: GroupScheduleUseCase,
    private val groupId: Int
): PagingSource<Int, Int>() {

    override fun getRefreshKey(state: PagingState<Int, Int>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null

        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Int> {
        val page = params.key ?: 1
        val pageSize = params.loadSize

        return when (
            val result = groupScheduleUseCase.getScheduleById(groupId, page, pageSize)
        ) {
            is Resource.Success -> {
                val data = result.data!!
                val nextKey = if (data.schedules.size < pageSize) null else page - 1
                val prevKey = if (page == 1) null else page - 1
                LoadResult.Page(ArrayList(), prevKey, nextKey)
            }
            is Resource.Error -> {
                LoadResult.Page(ArrayList(), null, null)
            }
        }
    }

}


