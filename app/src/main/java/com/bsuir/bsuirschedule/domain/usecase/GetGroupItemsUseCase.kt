package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.Faculty
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.Speciality
import com.bsuir.bsuirschedule.domain.repository.FacultyRepository
import com.bsuir.bsuirschedule.domain.repository.GroupItemsRepository
import com.bsuir.bsuirschedule.domain.repository.SpecialityRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.StatusCode
import kotlin.collections.ArrayList

class GetGroupItemsUseCase(
    private val groupItemsRepository: GroupItemsRepository,
    private val specialityRepository: SpecialityRepository,
    private val facultyRepository: FacultyRepository,
) {

    suspend fun getGroupItemsAPI(): Resource<ArrayList<Group>> {
        val result = groupItemsRepository.getGroupItemsAPI()

        return try {
            when(result) {
                is Resource.Success -> {
                    val groups = result.data!!
                    val isMergedSpecialities = mergeSpecialities(groups)
                    if (isMergedSpecialities is Resource.Error) {
                        return Resource.Error(
                            errorType = isMergedSpecialities.statusCode,
                            message = isMergedSpecialities.message
                        )
                    }
                    val isMergedFaculties = mergeFaculties(groups)
                    if (isMergedFaculties is Resource.Error) {
                        return Resource.Error(
                            errorType = isMergedFaculties.statusCode,
                            message = isMergedFaculties.message
                        )
                    }
                    Resource.Success(groups)
                }
                is Resource.Error -> {
                    Resource.Error(
                        errorType = result.statusCode,
                        message = result.message
                    )
                }
            }
        } catch (e: Exception) {
            Resource.Error(
                errorType = StatusCode.DATA_ERROR,
                message = e.message
            )
        }
    }

    private suspend fun mergeFaculties(groups: ArrayList<Group>): Resource<Unit> {
        val result = facultyRepository.getFaculties()

        return try {
            when(result) {
                is Resource.Success -> {
                    val faculties = result.data!!
                    groups.map { group ->
                        group.faculty = faculties.find { faculty ->
                            group.facultyId == faculty.id
                        } ?: Faculty.empty
                    }
                    Resource.Success(null)
                }
                is Resource.Error -> {
                    Resource.Error(
                        errorType = result.statusCode,
                        message = result.message
                    )
                }
            }
        } catch (e: Exception) {
            Resource.Error(
                errorType = StatusCode.DATA_ERROR,
                message = e.message
            )
        }
    }

    private suspend fun mergeSpecialities(groups: ArrayList<Group>): Resource<Unit> {
        val result = specialityRepository.getSpecialities()

        return try {
            when(result) {
                is Resource.Success -> {
                    val specialities = result.data!!
                    groups.map { group ->
                        group.speciality = specialities.find { speciality ->
                            group.specialityId == speciality.id
                        } ?: Speciality.empty
                    }
                    Resource.Success(null)
                }
                is Resource.Error -> {
                    Resource.Error(
                        errorType = result.statusCode,
                        message = result.message
                    )
                }
            }
        } catch (e: Exception) {
            Resource.Error(
                errorType = StatusCode.DATA_ERROR,
                message = e.message
            )
        }
    }

    suspend fun filterByKeywordASC(s: String): Resource<ArrayList<Group>> {
        return groupItemsRepository.filterByKeywordASC(s)
    }

    suspend fun filterByKeywordDESC(s: String): Resource<ArrayList<Group>> {
        return groupItemsRepository.filterByKeywordDESC(s)
    }

    suspend fun saveGroupItems(groups: ArrayList<Group>): Resource<Unit> {
        return try {
            when (
                val result = groupItemsRepository.saveGroupItemsList(groups)
            ) {
                is Resource.Success -> {
                    Resource.Success(null)
                }
                is Resource.Error -> {
                    Resource.Error(
                        errorType = result.statusCode,
                        message = result.message
                    )
                }
            }
        } catch (e: Exception) {
            Resource.Error(
                errorType = StatusCode.DATA_ERROR,
                message = e.message
            )
        }
    }

    suspend fun getAllGroupItems(): Resource<ArrayList<Group>> {
        return when (
            val result = groupItemsRepository.getAllGroupItems()
        ) {
            is Resource.Success -> {
                val data = result.data ?: ArrayList()
                Resource.Success(data)
            }
            is Resource.Error -> {
                Resource.Error(
                    errorType = result.statusCode,
                    message = result.message
                )
            }
        }
    }

}


