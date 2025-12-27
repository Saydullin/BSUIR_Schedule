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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupDao: GroupDao,
    private val getAndSaveAllGroupsUseCase: GetAndSaveAllGroupsUseCase,
    private val groupEntityToUiMapper: GroupEntityToUiMapper,
): ViewModel() {

    init {
        loadAllGroups()
    }

    private val _error = MutableSharedFlow<Resource.Error<Unit>?>()
    val error: SharedFlow<Resource.Error<Unit>?> = _error

    val groupsPagingFlow: Flow<PagingData<GroupUI>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            GroupPagingSource(
                dao = groupDao,
                groupEntityToUiMapper = groupEntityToUiMapper,
            )
        }
    ).flow.cachedIn(viewModelScope)

    fun loadAllGroups() {
        viewModelScope.launch {
            getAndSaveAllGroupsUseCase.execute()
                .onSuspendError { _error.emit(it) }
        }
    }

}


