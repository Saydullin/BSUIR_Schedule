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
            val page = params.key ?: 0
            val pageSize = params.loadSize
            val offset = page * pageSize

            val data = withContext(Dispatchers.IO) {
                dao.getPagingEmployees(
                    limit = pageSize,
                    offset = offset
                )
            }

            val employees = data.map { employeeWithDepartmentsEntityToUiMapper.map(it) }

            LoadResult.Page(
                data = employees,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (data.size < pageSize) null else page + 1
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
        val page = state.closestPageToPosition(anchor)

        return page?.prevKey?.plus(1)
            ?: page?.nextKey?.minus(1)
    }
}