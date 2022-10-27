package com.bsuir.bsuirschedule.domain.utils

sealed class Resource<T>(
    val data: T? = null,
    val errorType: Int = NO_ERROR,
    val message: String? = null
) {

    companion object {
        const val NO_ERROR = 0
        const val SERVER_ERROR = 1
        const val CONNECTION_ERROR = 2
        const val UPDATE_ERROR = 3
        const val DATABASE_ERROR = 4
        const val DATABASE_NOT_FOUND_ERROR = 5
        const val SYSTEM_ERROR = 6
        const val DATA_ERROR = 7
        const val UNKNOWN_ERROR = 8
        const val WEEK_API_LOADING_ERROR = 9

        const val SCHEDULE_LOADED_SUCCESS = 10
        const val SCHEDULE_DELETED_SUCCESS = 11
    }

    class Success<T>(data: T?): Resource<T>(data)

    class Error<T>(
        errorType: Int = UNKNOWN_ERROR,
        message: String? = "",
        data: T? = null
    ): Resource<T>(data, errorType, message)

}


