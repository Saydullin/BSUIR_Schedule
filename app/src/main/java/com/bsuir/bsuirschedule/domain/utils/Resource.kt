package com.bsuir.bsuirschedule.domain.utils

enum class StatusCode {
    NO_ERROR,
    SERVER_ERROR,
    CONNECTION_ERROR,
    UPDATE_ERROR,
    DATABASE_ERROR,
    DATABASE_NOT_FOUND_ERROR,
    SYSTEM_ERROR,
    DATA_ERROR,
    UNKNOWN_ERROR,
    WEEK_API_LOADING_ERROR,
    SCHEDULE_LOADED_SUCCESS,
    SCHEDULE_UPDATED_SUCCESS,
    SCHEDULE_DELETED_SUCCESS,
    SCHEDULE_SUBJECT_DELETED,
    SCHEDULE_SUBJECT_ADDED,
    SCHEDULE_SUBJECT_EDITED,
    SCHEDULE_SUBJECT_IGNORED,
    SCHEDULE_SUBJECT_NOT_IGNORED,
    ALL_SCHEDULES_UPDATED_SUCCESS,
    TEST_SCHEDULE_NOT_FOUND_PREF,
}

sealed class Resource<T>(
    val data: T? = null,
    val statusCode: StatusCode = StatusCode.NO_ERROR,
    val message: String? = null
) {

    class Success<T>(data: T?): Resource<T>(data)

    class Error<T>(
        statusCode: StatusCode = StatusCode.UNKNOWN_ERROR,
        message: String? = "",
        data: T? = null
    ): Resource<T>(data, statusCode, message)

}


