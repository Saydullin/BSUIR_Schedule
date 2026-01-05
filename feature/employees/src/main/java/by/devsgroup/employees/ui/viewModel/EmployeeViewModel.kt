package by.devsgroup.employees.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import by.devsgroup.database.employees.dao.EmployeeDao
import by.devsgroup.employees.mapper.EmployeeWithDepartmentsEntityToUiMapper
import by.devsgroup.employees.paging.EmployeePagingSource
import by.devsgroup.employees.ui.model.EmployeeUI
import by.devsgroup.employees.usecase.GetAndSaveAllEmployeesUseCase
import by.devsgroup.resource.Resource
import com.saydullin.departments.usecase.GetAndSaveAllDepartmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val employeeWithDepartmentsEntityToUiMapper: EmployeeWithDepartmentsEntityToUiMapper,
    private val getAndSaveAllDepartmentsUseCase: GetAndSaveAllDepartmentsUseCase,
    private val getAndSaveAllEmployeesUseCase: GetAndSaveAllEmployeesUseCase,
    private val employeeDao: EmployeeDao,
) : ViewModel() {

    private val _error = MutableSharedFlow<Resource.Error<Unit>?>()
    val error: SharedFlow<Resource.Error<Unit>?> = _error

    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val employeesPagingFlow: Flow<PagingData<EmployeeUI>> =
        trigger.flatMapLatest {
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    initialLoadSize = 20,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    EmployeePagingSource(
                        dao = employeeDao,
                        employeeWithDepartmentsEntityToUiMapper = employeeWithDepartmentsEntityToUiMapper,
                    )
                }
            ).flow
        }.cachedIn(viewModelScope)

    fun loadAllDepartmentsAndEmployees() {
        viewModelScope.launch {
            getAndSaveAllDepartmentsUseCase.execute()
                .onSuspendError { _error.emit(it) } ?: return@launch

            getAndSaveAllEmployeesUseCase.execute()
                .onSuspendError { _error.emit(it) } ?: return@launch

            trigger.emit(Unit)
        }
    }

}