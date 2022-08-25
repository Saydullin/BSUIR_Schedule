package com.example.bsuirschedule.domain.usecase

import android.util.Log
import com.example.bsuirschedule.domain.models.*
import com.example.bsuirschedule.domain.repository.GroupItemsRepository
import com.example.bsuirschedule.domain.repository.ScheduleRepository
import com.example.bsuirschedule.domain.utils.Resource

class GroupScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val groupItemsRepository: GroupItemsRepository,
    private val fullScheduleUseCase: FullScheduleUseCase,
    private val examsScheduleUseCase: FullExamsScheduleUseCase
) {

    suspend fun getGroupScheduleAPI(groupName: String): Resource<GroupSchedule> {

        return try {
            when (
                val apiSchedule = scheduleRepository.getScheduleAPI(groupName)
            ) {
                is Resource.Success -> {
                    val data = apiSchedule.data!!
                    data.id = data.group?.id ?: -1
                    val isMergedFacultyAndSpeciality = mergeSpecialitiesAndFaculties(data)
                    if (isMergedFacultyAndSpeciality is Resource.Error) {
                        return Resource.Error(
                            errorType = isMergedFacultyAndSpeciality.errorType,
                            message = isMergedFacultyAndSpeciality.message
                        )
                    }
                    Resource.Success(data)
                }
                is Resource.Error -> {
                    Resource.Error(
                        errorType = apiSchedule.errorType,
                        message = apiSchedule.message
                    )
                }
            }
        } catch (e: Exception) {
            return Resource.Error(
                errorType = Resource.DATA_ERROR,
                message = e.message
            )
        }
    }

    private suspend fun mergeSpecialitiesAndFaculties(groupSchedule: GroupSchedule): Resource<GroupSchedule> {
        return when (
            val result = groupItemsRepository.getAllGroupItems()
        ) {
            is Resource.Success -> {
                val data = result.data!!
                if (groupSchedule.group == null) {
                    return Resource.Error(
                        errorType = Resource.DATA_ERROR,
                        message = "Group field is null, cannot add specialities and faculties list"
                    )
                }
                val groupMatch = data.find { it.id == groupSchedule.group.id }
                groupSchedule.group.speciality = groupMatch?.speciality
                groupSchedule.group.faculty = groupMatch?.faculty
                Resource.Success(GroupSchedule.empty)
            }
            is Resource.Error -> {
                Resource.Error(
                    errorType = result.errorType,
                    message = result.message
                )
            }
        }
    }

    fun getFullSchedule(groupSchedule: GroupSchedule): Resource<Schedule> {
        return fullScheduleUseCase.getSchedule(groupSchedule)
    }

    suspend fun getScheduleById(groupId: Int): Resource<GroupSchedule> {
        return try {
            when (
                val result = scheduleRepository.getScheduleById(groupId)
            ) {
                is Resource.Success -> {
                    val data = result.data!!
                    if (data.exams?.isNotEmpty() == true) {
                        val exams = data.exams.toMutableList() as ArrayList // TODO make here
                        val examsSchedule = examsScheduleUseCase.getSchedule(exams)
                        data.examsSchedule = examsSchedule
                    }
                    Resource.Success(data)
                }
                is Resource.Error -> {
                    Resource.Error(
                        errorType = result.errorType,
                        message = result.message
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.DATA_ERROR,
                message = e.message
            )
        }
    }

    suspend fun saveSchedule(schedule: GroupSchedule): Resource<Unit> {
        return scheduleRepository.saveSchedule(schedule)
    }

    suspend fun deleteSchedule(savedSchedule: SavedSchedule): Resource<Unit> {
        return scheduleRepository.deleteGroupSchedule(savedSchedule.group.name)
    }

}


