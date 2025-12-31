package by.devsgroup.schedule.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import by.devsgroup.database.schedule.dao.ScheduleDayDao
import by.devsgroup.domain.model.schedule.full.FullScheduleDay
import by.devsgroup.schedule.mapper.entityToDomain.DaysWithLessonsEntityToDomainMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SchedulePagingSource(
    private val scheduleId: Long,
    private val filterMillis: Long,
    private val dao: ScheduleDayDao,
    private val daysWithLessonsEntityToDomainMapper: DaysWithLessonsEntityToDomainMapper,
) : PagingSource<Int, FullScheduleDay>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, FullScheduleDay> {
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize

            println("SchedulePagingSource page $page, pageSize $pageSize")

            val offset = page * pageSize

            val data = withContext(Dispatchers.IO) {
                dao.getPagingDays(
                    filterMillis = filterMillis,
                    scheduleId = scheduleId,
                    limit = pageSize,
                    offset = offset,
                )
            }

            val groups = data.map { daysWithLessonsEntityToDomainMapper.map(it) }

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
        state: PagingState<Int, FullScheduleDay>
    ): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor)

        return page?.prevKey?.plus(1)
            ?: page?.nextKey?.minus(1)
    }
}


