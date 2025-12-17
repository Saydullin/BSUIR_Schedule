package by.devsgroup.groups.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.devsgroup.domain.model.groups.Group
import by.devsgroup.domain.repository.groups.GroupDatabaseRepository
import by.devsgroup.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupDatabaseRepository: GroupDatabaseRepository
): ViewModel() {

    private val _groups = MutableStateFlow<List<Group>?>(null)
    val groups: StateFlow<List<Group>?> = _groups

    private val _error = MutableSharedFlow<Resource.Error<Unit>?>()
    val error: SharedFlow<Resource.Error<Unit>?> = _error

    init {
        loadAllGroups()
    }

    fun loadAllGroups() {
        viewModelScope.launch {
            val groups = groupDatabaseRepository.getAllGroups()
                .onSuspendError { _error.emit(it) } ?: return@launch

            _groups.value = groups
        }
    }

}


