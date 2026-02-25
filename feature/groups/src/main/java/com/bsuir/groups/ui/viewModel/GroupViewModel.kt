package com.bsuir.groups.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bsuir.database.groups.dao.GroupDao
import com.bsuir.domain.model.error.ErrorType
import com.bsuir.domain.status.loading.LoadingStatus
import com.bsuir.groups.mapper.GroupEntityToUiMapper
import com.bsuir.groups.paging.GroupPagingSource
import com.bsuir.groups.ui.model.GroupUI
import com.bsuir.groups.usecase.GetAndSaveAllGroupsUseCase
import com.bsuir.resource.Resource
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
class GroupViewModel @Inject constructor(
    private val groupDao: GroupDao,
    private val getAndSaveAllGroupsUseCase: GetAndSaveAllGroupsUseCase,
    private val groupEntityToUiMapper: GroupEntityToUiMapper,
) : ViewModel() {

    private val _error = MutableSharedFlow<Resource.Error<Unit>?>()
    val error: SharedFlow<Resource.Error<Unit>?> = _error

    private val _currentSearch = MutableStateFlow("")
    val currentSearch: StateFlow<String> = _currentSearch

    private val _allGroupsLoading = MutableStateFlow<LoadingStatus<Unit>?>(null)
    val allGroupsLoading: StateFlow<LoadingStatus<Unit>?> = _allGroupsLoading

    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val groupsPagingFlow: Flow<PagingData<GroupUI>> = combine(
        trigger,
        _currentSearch
    ) { trigger, currentSearch ->
        Pair(trigger, currentSearch)
    }.flatMapLatest { (_, search) ->
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    initialLoadSize = 20,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    GroupPagingSource(
                        dao = groupDao,
                        search = search,
                        groupEntityToUiMapper = groupEntityToUiMapper,
                    )
                }
            ).flow
        }.cachedIn(viewModelScope)

    fun loadAllGroups() {
        viewModelScope.launch {
            _allGroupsLoading.value = LoadingStatus.Loading

            getAndSaveAllGroupsUseCase.execute()
                .onSuspendError {
                    _error.emit(it)
                    _allGroupsLoading.value = LoadingStatus.Error(ErrorType.UnknownError)
                } ?: return@launch

            _allGroupsLoading.value = LoadingStatus.Success(Unit)
            trigger.emit(Unit)
        }
    }

    fun updateGroupsList() {
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }

    fun setSearch(search: String) {
        _currentSearch.value = search
    }

}


