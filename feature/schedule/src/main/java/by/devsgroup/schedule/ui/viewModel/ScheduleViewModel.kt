package by.devsgroup.schedule.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import by.devsgroup.database.schedule.dao.ScheduleDayDao
import by.devsgroup.domain.model.schedule.full.FullSchedule
import by.devsgroup.domain.model.schedule.full.FullScheduleDay
import by.devsgroup.domain.repository.schedule.ScheduleDatabaseRepository
import by.devsgroup.resource.Resource
import by.devsgroup.schedule.mapper.entityToDomain.DaysWithLessonsEntityToDomainMapper
import by.devsgroup.schedule.paging.SchedulePagingSource
import by.devsgroup.schedule.usecase.GetAndSaveGroupScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleDayDao: ScheduleDayDao,
    private val scheduleDatabaseRepository: ScheduleDatabaseRepository,
    private val getAndSaveGroupScheduleUseCase: GetAndSaveGroupScheduleUseCase,
    private val daysWithLessonsEntityToDomainMapper: DaysWithLessonsEntityToDomainMapper,
) : ViewModel() {

    private val _currentSchedule = MutableStateFlow<FullSchedule?>(null)
    val currentSchedule: StateFlow<FullSchedule?> = _currentSchedule

    private val _error = MutableSharedFlow<Resource.Error<Unit>?>()
    val error: SharedFlow<Resource.Error<Unit>?> = _error

    private val currentScheduleId = MutableStateFlow<Long?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val scheduleDaysFlow: Flow<PagingData<FullScheduleDay>> =
        currentScheduleId
            .filterNotNull()
            .flatMapLatest { scheduleId ->
                Pager(
                    config = PagingConfig(
                        pageSize = 5,
                        initialLoadSize = 10,
                        enablePlaceholders = true
                    ),
                    pagingSourceFactory = {
                        SchedulePagingSource(
                            scheduleId = scheduleId,
                            filterMillis = System.currentTimeMillis(),
                            dao = scheduleDayDao,
                            daysWithLessonsEntityToDomainMapper = daysWithLessonsEntityToDomainMapper,
                        )
                    }
                ).flow
            }.cachedIn(viewModelScope)

    fun loadSchedule() {
        viewModelScope.launch {
            getAndSaveGroupScheduleUseCase.execute("253505")
        }
    }

    fun getSchedule() {
        viewModelScope.launch {
            val schedule = scheduleDatabaseRepository.getFullScheduleById(23875)
                .onSuspendError {
                    _error.emit(it)

                    println(it.getStatusAndMessage())
                }

            _currentSchedule.value = schedule
        }
    }

    fun setCurrentScheduleId(scheduleId: Long) {
        viewModelScope.launch {
            println("setCurrentScheduleId $scheduleId")
            currentScheduleId.value = scheduleId
        }
    }

}