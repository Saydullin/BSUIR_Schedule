package com.bsuir.bsuirschedule.presentation.utils

import android.content.Context
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.models.ErrorMessageText
import com.bsuir.bsuirschedule.domain.utils.StatusCode

class ErrorMessage(private val context: Context) {

    fun get(type: StatusCode): ErrorMessageText {
        val resources = context.resources
        return when(type) {
            StatusCode.DATA_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.data_error),
                    caption = resources.getString(R.string.data_error_caption)
                )
            }
            StatusCode.UPDATE_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.update_error),
                    caption = resources.getString(R.string.update_error_caption)
                )
            }
            StatusCode.CONNECTION_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.connection_error),
                    caption = resources.getString(R.string.connection_error_caption)
                )
            }
            StatusCode.SERVER_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.server_error),
                    caption = resources.getString(R.string.server_error_caption)
                )
            }
            StatusCode.SYSTEM_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.system_error),
                    caption = resources.getString(R.string.system_error_caption)
                )
            }
            StatusCode.DATABASE_NOT_FOUND_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.database_not_found_error),
                    caption = resources.getString(R.string.database_not_found_error_caption)
                )
            }
            StatusCode.DATABASE_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.database_error),
                    caption = resources.getString(R.string.database_error_caption)
                )
            }
            StatusCode.NO_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.no_error),
                    caption = resources.getString(R.string.no_error_caption)
                )
            }
            StatusCode.SCHEDULE_LOADED_SUCCESS -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_success_loaded),
                    caption = resources.getString(R.string.schedule_success_loaded_caption)
                )
            }
            StatusCode.SCHEDULE_UPDATED_SUCCESS -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_success_updated),
                    caption = resources.getString(R.string.schedule_success_updated_caption)
                )
            }
            StatusCode.SCHEDULE_DELETED_SUCCESS -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_success_deleted),
                    caption = resources.getString(R.string.schedule_success_deleted_caption)
                )
            }
            StatusCode.WEEK_API_LOADING_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_week_loaded_error),
                    caption = resources.getString(R.string.schedule_week_loaded_error_caption)
                )
            }
            StatusCode.SCHEDULE_SUBJECT_EDITED -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_subject_edited),
                    caption = resources.getString(R.string.schedule_subject_edited_caption)
                )
            }
            StatusCode.SCHEDULE_SUBJECT_DELETED -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_subject_deleted),
                    caption = resources.getString(R.string.schedule_subject_deleted_caption)
                )
            }
            StatusCode.SCHEDULE_SUBJECT_ADDED -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_subject_added),
                    caption = resources.getString(R.string.schedule_subject_added_caption)
                )
            }
            StatusCode.SCHEDULE_SUBJECT_IGNORED -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_subject_ignored),
                    caption = resources.getString(R.string.schedule_subject_ignored_caption)
                )
            }
            StatusCode.SCHEDULE_SUBJECT_NOT_IGNORED -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_subject_not_ignored),
                    caption = resources.getString(R.string.schedule_subject_not_ignored_caption)
                )
            }
            StatusCode.ALL_SCHEDULES_UPDATED_SUCCESS -> {
                ErrorMessageText(
                    title = resources.getString(R.string.all_schedules_updated_success),
                    caption = resources.getString(R.string.all_schedules_updated_success_caption)
                )
            }
            else -> {
                ErrorMessageText(
                    title = resources.getString(R.string.unknown_error),
                    caption = resources.getString(R.string.unknown_error_caption)
                )
            }
        }
    }

}


