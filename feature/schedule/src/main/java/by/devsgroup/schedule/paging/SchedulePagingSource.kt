package by.devsgroup.schedule.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import by.devsgroup.database.schedule.dao.ScheduleDayDao
import by.devsgroup.domain.model.schedule.full.FullScheduleDay
import by.devsgroup.schedule.mapper.entityToDomain.DaysWithLessonsEntityToDomainMapper
import by.devsgroup.schedule.ui.model.ScheduleDateFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId

class SchedulePagingSource(
    private val scheduleId: Long,
    private val dateFilter: ScheduleDateFilter = ScheduleDateFilter.FromNow,
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
                val filterMillis = when (dateFilter) {
                    is ScheduleDateFilter.FromNow -> {
                        LocalDate.now()
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                    }

                    is ScheduleDateFilter.From -> {
                        dateFilter.dateMillis
                    }

                    is ScheduleDateFilter.Until -> {
                        dateFilter.dateMillis
                    }
                }

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


