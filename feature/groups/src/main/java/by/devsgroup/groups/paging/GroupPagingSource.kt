package by.devsgroup.groups.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import by.devsgroup.database.groups.dao.GroupDao
import by.devsgroup.groups.mapper.GroupEntityToUiMapper
import by.devsgroup.groups.ui.model.GroupUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GroupPagingSource(
    private val dao: GroupDao,
    private val search: String,
    private val groupEntityToUiMapper: GroupEntityToUiMapper,
) : PagingSource<Int, GroupUI>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, GroupUI> {
        return try {
            val offset = params.key ?: 0
            val limit = params.loadSize

            println("GroupPagingSource limit = $limit, offset = $offset")

            val data = withContext(Dispatchers.IO) {
                dao.getPagingGroups(
                    searchLike = "%$search%",
                    limit = limit,
                    offset = offset
                )
            }

            val groups = data.map(groupEntityToUiMapper::map)

            LoadResult.Page(
                data = groups,
                prevKey = if (offset == 0) null else maxOf(0, offset - limit),
                nextKey = if (data.size < limit) null else offset + data.size
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, GroupUI>
    ): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null

        return page.prevKey?.plus(state.config.pageSize)
            ?: page.nextKey?.minus(state.config.pageSize)
    }

}


