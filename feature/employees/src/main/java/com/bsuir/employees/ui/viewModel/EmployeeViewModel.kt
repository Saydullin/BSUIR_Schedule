package com.bsuir.employees.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bsuir.database.employees.dao.EmployeeDao
import com.bsuir.domain.model.error.ErrorType
import com.bsuir.domain.status.loading.LoadingStatus
import com.bsuir.employees.mapper.EmployeeWithDepartmentsEntityToUiMapper
import com.bsuir.employees.paging.EmployeePagingSource
import com.bsuir.employees.ui.model.EmployeeUI
import com.bsuir.employees.usecase.GetAndSaveAllEmployeesUseCase
import com.bsuir.resource.Resource
import com.saydullin.departments.usecase.GetAndSaveAllDepartmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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

    private val _currentSearch = MutableStateFlow("")
    val currentSearch: StateFlow<String> = _currentSearch

    private val _allEmployeesLoading = MutableStateFlow<LoadingStatus<Unit>?>(null)
    val allEmployeesLoading: StateFlow<LoadingStatus<Unit>?> = _allEmployeesLoading

    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val employeesPagingFlow: Flow<PagingData<EmployeeUI>> = combine(
        trigger,
        _currentSearch
    ) { trigger, search ->
       Pair(trigger, search)
    }.flatMapLatest { (_, search) ->
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    initialLoadSize = 20,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    EmployeePagingSource(
                        dao = employeeDao,
                        search = search,
                        employeeWithDepartmentsEntityToUiMapper = employeeWithDepartmentsEntityToUiMapper,
                    )
                }
            ).flow
        }.cachedIn(viewModelScope)

    fun loadAllDepartmentsAndEmployees() {
        viewModelScope.launch {
            _allEmployeesLoading.value = LoadingStatus.Loading

            getAndSaveAllDepartmentsUseCase.execute()
                .onSuspendError {
                    _error.emit(it)
                    _allEmployeesLoading.value = LoadingStatus.Error(ErrorType.UnknownError)
                } ?: return@launch

            getAndSaveAllEmployeesUseCase.execute()
                .onSuspendError {
                    _error.emit(it)
                    _allEmployeesLoading.value = LoadingStatus.Error(ErrorType.UnknownError)
                } ?: return@launch

            _allEmployeesLoading.value = LoadingStatus.Success(Unit)
            trigger.emit(Unit)
        }
    }

    fun setSearch(search: String) {
        _currentSearch.value = search
    }

}