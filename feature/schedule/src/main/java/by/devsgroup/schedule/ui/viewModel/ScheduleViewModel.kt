package by.devsgroup.schedule.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.devsgroup.domain.repository.schedule.ScheduleDatabaseRepository
import by.devsgroup.schedule.usecase.GetAndSaveGroupScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getAndSaveGroupScheduleUseCase: GetAndSaveGroupScheduleUseCase,
    private val scheduleDatabaseRepository: ScheduleDatabaseRepository,
): ViewModel() {

    init {
        loadSchedule()
    }

    fun loadSchedule() {
        viewModelScope.launch {
            getAndSaveGroupScheduleUseCase.execute("253505")
        }
    }

    fun getSchedule() {
        viewModelScope.launch {
            val schedule = scheduleDatabaseRepository.getScheduleById(23875)

            println("schedule ${schedule.data}")
        }
    }

}