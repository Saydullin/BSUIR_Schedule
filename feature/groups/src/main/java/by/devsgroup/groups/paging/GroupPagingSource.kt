package by.devsgroup.groups.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import by.devsgroup.domain.model.groups.Group
import by.devsgroup.groups.data.db.dao.GroupDao
import by.devsgroup.groups.data.mapper.GroupEntityToDomainMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GroupPagingSource(
    private val dao: GroupDao,
    private val groupEntityToDomainMapper: GroupEntityToDomainMapper,
) : PagingSource<Int, Group>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Group> {
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize
            val offset = page * pageSize

            val data = withContext(Dispatchers.IO) {
                dao.getPagingGroups(
                    limit = pageSize,
                    offset = offset
                )
            }

            val groups = data.map { groupEntityToDomainMapper.map(it) }

            LoadResult.Page(
                data = groups,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (data.size < pageSize) null else page + 1
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, Group>
    ): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor)

        return page?.prevKey?.plus(1)
            ?: page?.nextKey?.minus(1)
    }
}