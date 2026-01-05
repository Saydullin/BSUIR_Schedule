package by.devsgroup.schedule.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.devsgroup.domain.model.schedule.common.ScheduleType
import by.devsgroup.domain.repository.schedule.ScheduleDatabaseRepository
import by.devsgroup.resource.Resource
import by.devsgroup.schedule.ext.getScheduleType
import by.devsgroup.schedule.ui.model.PreviewScheduleType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewScheduleViewModel @Inject constructor(
    private val scheduleDatabaseRepository: ScheduleDatabaseRepository
) : ViewModel() {

    private val _previewSchedules = MutableStateFlow<List<PreviewScheduleType>?>(null)
    val previewSchedules: StateFlow<List<PreviewScheduleType>?> = _previewSchedules

    private val _error = MutableSharedFlow<Resource.Error<Unit>?>()
    val error: SharedFlow<Resource.Error<Unit>?> = _error

    fun getAllSchedules() {
        viewModelScope.launch {
            val previewSchedules = scheduleDatabaseRepository.getAllPreviewSchedules()
                .onSuspendError { _error.emit(it) }

            val uiSchedule = previewSchedules?.mapNotNull { schedule ->
                val type = schedule.getScheduleType()

                when (type) {
                    ScheduleType.GROUP -> {
                        val name = schedule.group?.name
                        name?.let {
                            PreviewScheduleType.Group(
                                name = it
                            )
                        }
                    }

                    ScheduleType.EMPLOYEE -> {
                        val firstName = schedule.employee?.firstName
                        val middleName = schedule.employee?.middleName
                        val lastName = schedule.employee?.lastName

                        if (!firstName.isNullOrEmpty() && !middleName.isNullOrEmpty() && !lastName.isNullOrEmpty()) {
                            PreviewScheduleType.Employee(
                                firstName = firstName,
                                middleName = middleName,
                                lastName = lastName,
                                image = schedule.employee?.photoLink
                            )
                        } else {
                            null
                        }
                    }

                    else -> {
                        null
                    }
                }
            }

            _previewSchedules.value = uiSchedule
        }

    }
}