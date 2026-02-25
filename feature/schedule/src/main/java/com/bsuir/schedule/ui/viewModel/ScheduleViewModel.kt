package com.bsuir.schedule.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bsuir.database.schedule.dao.ScheduleDayDao
import com.bsuir.domain.model.error.ErrorType
import com.bsuir.domain.model.schedule.full.FullSchedule
import com.bsuir.domain.model.schedule.full.FullScheduleDay
import com.bsuir.domain.model.schedule.preview.PreviewSchedule
import com.bsuir.domain.repository.schedule.ScheduleDatabaseRepository
import com.bsuir.domain.status.loading.LoadingStatus
import com.bsuir.resource.Resource
import com.bsuir.schedule.mapper.entityToDomain.DaysWithLessonsEntityToDomainMapper
import com.bsuir.schedule.paging.SchedulePagingSource
import com.bsuir.schedule.ui.model.ScheduleDateFilter
import com.bsuir.schedule.usecase.GetAndSaveEmployeeScheduleUseCase
import com.bsuir.schedule.usecase.GetAndSaveGroupScheduleUseCase
import com.bsuir.schedule.usecase.GetSchedulePreviewByIdUseCase
import com.bsuir.week.usecase.GetOrLoadWeekUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleDayDao: ScheduleDayDao,
    private val getOrLoadWeekUseCase: GetOrLoadWeekUseCase,
    private val scheduleDatabaseRepository: ScheduleDatabaseRepository,
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
            .flatMapLatest { scheduleId ->
                if (scheduleId == null) {
                    flowOf(PagingData.empty())
                } else {
                    Pager(
                        config = PagingConfig(
                            pageSize = 5,
                            initialLoadSize = 10,
                            enablePlaceholders = true
                        ),
                        pagingSourceFactory = {
                            SchedulePagingSource(
                                scheduleId = scheduleId,
                                dateFilter = ScheduleDateFilter.FromNow,
                                dao = scheduleDayDao,
                                daysWithLessonsEntityToDomainMapper = daysWithLessonsEntityToDomainMapper,
                            )
                        }
                    ).flow
                }
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

    fun setCurrentScheduleByEmployeeUrlId(employeeId: Long) {
        viewModelScope.launch {
            val scheduleId = scheduleDatabaseRepository.getScheduleIdByEmployeeId(employeeId)
                .onSuspendError { _error.emit(it) } ?: return@launch

            _currentScheduleId.value = scheduleId

            getSchedulePreviewById(scheduleId)
        }
    }

    fun setCurrentScheduleByGroupId(groupId: Long) {
        viewModelScope.launch {
            val scheduleId = scheduleDatabaseRepository.getScheduleIdByGroupId(groupId)
                .onSuspendError { _error.emit(it) } ?: return@launch

            _currentScheduleId.value = scheduleId

            getSchedulePreviewById(scheduleId)
        }
    }

    fun deleteScheduleByGroupId(scheduleId: Long) {
        viewModelScope.launch {
            scheduleDatabaseRepository.deleteScheduleByGroupId(scheduleId)
                .onSuspendError { _error.emit(it) } ?: return@launch
        }
    }

    fun deleteScheduleByEmployeeId(scheduleId: Long) {
        viewModelScope.launch {
            scheduleDatabaseRepository.deleteScheduleByEmployeeId(scheduleId)
                .onSuspendError { _error.emit(it) } ?: return@launch

            if (_currentScheduleId.value == scheduleId) {
                _currentSchedule.value = null
                _currentScheduleId.value = null
                _currentSchedulePreview.value = null
                _scheduleLoaded.emit(Unit)
            }
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