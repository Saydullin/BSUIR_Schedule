package by.devsgroup.schedule.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.devsgroup.domain.model.schedule.full.FullSchedule
import by.devsgroup.domain.repository.schedule.ScheduleDatabaseRepository
import by.devsgroup.resource.Resource
import by.devsgroup.schedule.usecase.GetAndSaveGroupScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getAndSaveGroupScheduleUseCase: GetAndSaveGroupScheduleUseCase,
    private val scheduleDatabaseRepository: ScheduleDatabaseRepository,
): ViewModel() {

    private val _currentSchedule = MutableStateFlow<FullSchedule?>(null)
    val currentSchedule: StateFlow<FullSchedule?> = _currentSchedule

    private val _error = MutableSharedFlow<Resource.Error<Unit>?>()
    val error: SharedFlow<Resource.Error<Unit>?> = _error

    fun loadSchedule() {
        viewModelScope.launch {
            getAndSaveGroupScheduleUseCase.execute("253505")
        }
    }

    fun getSchedule() {
        viewModelScope.launch {
            val schedule = scheduleDatabaseRepository.getScheduleById(23875)
                .onSuspendError {
                    _error.emit(it)

                    println(it.getStatusAndMessage())
                }

            _currentSchedule.value = schedule
        }
    }

}