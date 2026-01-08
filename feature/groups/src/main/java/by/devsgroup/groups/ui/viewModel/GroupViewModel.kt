package by.devsgroup.groups.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import by.devsgroup.database.groups.dao.GroupDao
import by.devsgroup.groups.mapper.GroupEntityToUiMapper
import by.devsgroup.groups.paging.GroupPagingSource
import by.devsgroup.groups.ui.model.GroupUI
import by.devsgroup.groups.usecase.GetAndSaveAllGroupsUseCase
import by.devsgroup.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
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
            getAndSaveAllGroupsUseCase.execute()
                .onSuspendError { _error.emit(it) } ?: return@launch

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


