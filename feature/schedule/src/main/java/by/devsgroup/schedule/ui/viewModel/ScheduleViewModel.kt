package by.devsgroup.schedule.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.devsgroup.schedule.usecase.GetAndSaveGroupScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getAndSaveGroupScheduleUseCase: GetAndSaveGroupScheduleUseCase,
): ViewModel() {

    init {
        loadSchedule()
    }

    fun loadSchedule() {
        viewModelScope.launch {
            getAndSaveGroupScheduleUseCase.execute("253505")
        }
    }

}