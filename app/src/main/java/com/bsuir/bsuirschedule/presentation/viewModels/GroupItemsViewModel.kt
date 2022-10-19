package com.bsuir.bsuirschedule.presentation.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsuir.bsuirschedule.domain.models.StateStatus
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.usecase.GetGroupItemsUseCase
import com.bsuir.bsuirschedule.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupItemsViewModel(
    private val getGroupItemsUseCase: GetGroupItemsUseCase
) : ViewModel() {

    private val allGroupItemsLoading = MutableLiveData(false)
    private val isUpdating = MutableLiveData(false)
    private val error = MutableLiveData<StateStatus>(null)
    private val allGroupItems = MutableLiveData<ArrayList<Group>>(null)
    val isUpdatingStatus = isUpdating
    val errorStatus = error
    val allGroupItemsStatus = allGroupItems

    fun closeError() {
        error.value = null
    }

    fun saveGroupItem(group: Group) {
        viewModelScope.launch(Dispatchers.IO) {
            getGroupItemsUseCase.saveGroupItems(arrayListOf(group))
        }
    }

    // Update all group items from API
    fun updateGroupItems() {
        isUpdating.value = true
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val newGroupItems = getGroupItemsUseCase.getGroupItemsAPI()
            ) {
                is Resource.Success -> {
                    when (
                        getGroupItemsUseCase.saveGroupItems(newGroupItems.data!!)
                    ) {
                        is Resource.Success -> {
                            getAllGroupItems()
                        }
                        is Resource.Error -> {
                            error.postValue(StateStatus(
                                state = StateStatus.ERROR_STATE,
                                type = newGroupItems.errorType,
                                message = newGroupItems.message
                            ))
                        }
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = newGroupItems.errorType,
                        message = newGroupItems.message
                    ))
                }
            }
            isUpdating.postValue(false)
        }
    }

    fun filterByKeyword(s: String, isAsc: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = if (isAsc) {
                getGroupItemsUseCase.filterByKeywordASC(s)
            } else {
                getGroupItemsUseCase.filterByKeywordDESC(s)
            }
            when(result) {
                is Resource.Success -> {
                    allGroupItems.postValue(result.data)
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.errorType,
                        message = result.message
                    ))
                }
            }
        }
    }

    // All Group Items
    fun getAllGroupItems() {
        viewModelScope.launch(Dispatchers.IO) {
            allGroupItemsLoading.postValue(true)
            when (
                val result = getGroupItemsUseCase.getAllGroupItems()
            ) {
                is Resource.Success -> {
                    allGroupItems.postValue(result.data)
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.errorType,
                        message = result.message
                    ))
                }
            }
            allGroupItemsLoading.postValue(false)
        }
    }

}


