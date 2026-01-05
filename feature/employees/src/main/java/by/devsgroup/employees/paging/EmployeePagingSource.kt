package by.devsgroup.employees.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import by.devsgroup.database.employees.dao.EmployeeDao
import by.devsgroup.employees.mapper.EmployeeWithDepartmentsEntityToUiMapper
import by.devsgroup.employees.ui.model.EmployeeUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmployeePagingSource(
    private val dao: EmployeeDao,
    private val employeeWithDepartmentsEntityToUiMapper: EmployeeWithDepartmentsEntityToUiMapper,
) : PagingSource<Int, EmployeeUI>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, EmployeeUI> {
        return try {
            val offset = params.key ?: 0
            val limit = params.loadSize

            println("EmployeePagingSource limit = $limit, offset = $offset")

            val data = withContext(Dispatchers.IO) {
                dao.getPagingEmployees(
                    limit = limit,
                    offset = offset
                )
            }

            val employees = data.map { employeeWithDepartmentsEntityToUiMapper.map(it) }

            LoadResult.Page(
                data = employees,
                prevKey = if (offset == 0) null else maxOf(0, offset - limit),
                nextKey = if (data.size < limit) null else offset + data.size
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, EmployeeUI>
    ): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null

        return page.prevKey?.plus(state.config.pageSize)
            ?: page.nextKey?.minus(state.config.pageSize)
    }
}