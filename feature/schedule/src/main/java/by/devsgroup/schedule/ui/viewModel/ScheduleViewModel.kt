package by.devsgroup.schedule.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import by.devsgroup.database.schedule.dao.ScheduleDayDao
import by.devsgroup.domain.model.error.ErrorType
import by.devsgroup.domain.model.schedule.full.FullSchedule
import by.devsgroup.domain.model.schedule.full.FullScheduleDay
import by.devsgroup.domain.model.schedule.preview.PreviewSchedule
import by.devsgroup.domain.status.loading.LoadingStatus
import by.devsgroup.resource.Resource
import by.devsgroup.schedule.mapper.entityToDomain.DaysWithLessonsEntityToDomainMapper
import by.devsgroup.schedule.paging.SchedulePagingSource
import by.devsgroup.schedule.usecase.GetAndSaveEmployeeScheduleUseCase
import by.devsgroup.schedule.usecase.GetAndSaveGroupScheduleUseCase
import by.devsgroup.schedule.usecase.GetSchedulePreviewByIdUseCase
import by.devsgroup.week.usecase.GetOrLoadWeekUseCase
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
    private val getOrLoadWeekUseCase: GetOrLoadWeekUseCase,
    private val getSchedulePreviewByIdUseCase: GetSchedulePreviewByIdUseCase,
    private val getAndSaveGroupScheduleUseCase: GetAndSaveGroupScheduleUseCase,
    private val getAndSaveEmployeeScheduleUseCase: GetAndSaveEmployeeScheduleUseCase,
    private val daysWithLessonsEntityToDomainMapper: DaysWithLessonsEntityToDomainMapper,
) : ViewModel() {

    private val _currentSchedule = MutableStateFlow<FullSchedule?>(null)
    val currentSchedule: StateFlow<FullSchedule?> = _currentSchedule

    private val _currentSchedulePreview = MutableStateFlow<LoadingStatus<PreviewSchedule>?>(null)
    val currentSchedulePreview: StateFlow<LoadingStatus<PreviewSchedule>?> = _currentSchedulePreview

    private val _scheduleLoaded = MutableSharedFlow<Unit?>()
    val scheduleLoaded: SharedFlow<Unit?> = _scheduleLoaded

    private val _error = MutableSharedFlow<Resource.Error<Unit>?>()
    val error: SharedFlow<Resource.Error<Unit>?> = _error

    private val _currentScheduleId = MutableStateFlow<Long?>(null)
    val currentScheduleId: StateFlow<Long?> = _currentScheduleId

    @OptIn(ExperimentalCoroutinesApi::class)
    val scheduleDaysFlow: Flow<PagingData<FullScheduleDay>> =
        _currentScheduleId
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

    fun loadGroupSchedule(groupName: String) {
        viewModelScope.launch {
            val week = getOrLoadWeekUseCase.execute()
                .onSuspendError { _error.emit(it) } ?: return@launch

            getAndSaveGroupScheduleUseCase.execute(
                currentWeek = week.week,
                groupName = groupName
            ).onSuspendError { _error.emit(it) } ?: return@launch

            _scheduleLoaded.emit(Unit)
        }
    }

    fun loadEmployeeSchedule(urlId: String) {
        viewModelScope.launch {
            val week = getOrLoadWeekUseCase.execute()
                .onSuspendError { _error.emit(it) } ?: return@launch

            getAndSaveEmployeeScheduleUseCase.execute(
                currentWeek = week.week,
                urlId = urlId,
            ).onSuspendError { _error.emit(it) } ?: return@launch

            _scheduleLoaded.emit(Unit)
        }
    }

    fun setCurrentScheduleId(scheduleId: Long) {
        viewModelScope.launch {
            println("setCurrentScheduleId $scheduleId")

            _currentScheduleId.value = scheduleId

            getSchedulePreviewById(scheduleId)
        }
    }

    private suspend fun getSchedulePreviewById(scheduleId: Long) {
        _currentSchedulePreview.value = LoadingStatus.Loading

        val currentSchedulePreview = getSchedulePreviewByIdUseCase.execute(scheduleId)
            .onError {
                _currentSchedulePreview.value = LoadingStatus.Error(ErrorType.DatabaseError)
            } ?: return

        _currentSchedulePreview.value = LoadingStatus.Success(currentSchedulePreview)
    }

}