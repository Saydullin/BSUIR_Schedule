package by.devsgroup.schedule.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.devsgroup.domain.repository.schedule.ScheduleServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleServerRepository: ScheduleServerRepository
): ViewModel() {

    init {
        loadSchedule()
    }

    fun loadSchedule() {
        viewModelScope.launch {
            scheduleServerRepository.getGroupSchedule("253505")
        }
    }

}