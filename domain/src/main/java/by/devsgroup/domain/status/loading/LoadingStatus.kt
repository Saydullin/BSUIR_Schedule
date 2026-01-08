package by.devsgroup.domain.status.loading

import by.devsgroup.domain.model.error.ErrorType

sealed class LoadingStatus<out T> {

    data object Loading : LoadingStatus<Nothing>()

    data class Success<T>(val data: T) : LoadingStatus<T>()

    data class Error(val type: ErrorType = ErrorType.UnknownError) : LoadingStatus<Nothing>()

}