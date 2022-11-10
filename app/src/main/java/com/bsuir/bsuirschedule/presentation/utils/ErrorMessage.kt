package com.bsuir.bsuirschedule.presentation.utils

import android.content.Context
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.models.ErrorMessageText
import com.bsuir.bsuirschedule.domain.utils.Resource

class ErrorMessage(private val context: Context) {

    fun get(type: Int): ErrorMessageText {
        val resources = context.resources
        return when(type) {
            Resource.DATA_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.data_error),
                    caption = resources.getString(R.string.data_error_caption)
                )
            }
            Resource.UPDATE_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.update_error),
                    caption = resources.getString(R.string.update_error_caption)
                )
            }
            Resource.CONNECTION_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.connection_error),
                    caption = resources.getString(R.string.connection_error_caption)
                )
            }
            Resource.SERVER_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.server_error),
                    caption = resources.getString(R.string.server_error_caption)
                )
            }
            Resource.SYSTEM_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.system_error),
                    caption = resources.getString(R.string.system_error_caption)
                )
            }
            Resource.DATABASE_NOT_FOUND_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.database_not_found_error),
                    caption = resources.getString(R.string.database_not_found_error_caption)
                )
            }
            Resource.DATABASE_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.database_error),
                    caption = resources.getString(R.string.database_error_caption)
                )
            }
            Resource.NO_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.no_error),
                    caption = resources.getString(R.string.no_error_caption)
                )
            }
            Resource.SCHEDULE_LOADED_SUCCESS -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_success_loaded),
                    caption = resources.getString(R.string.schedule_success_loaded_caption)
                )
            }
            Resource.SCHEDULE_UPDATED_SUCCESS -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_success_updated),
                    caption = resources.getString(R.string.schedule_success_updated_caption)
                )
            }
            Resource.SCHEDULE_DELETED_SUCCESS -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_success_deleted),
                    caption = resources.getString(R.string.schedule_success_deleted_caption)
                )
            }
            Resource.WEEK_API_LOADING_ERROR -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_week_loaded_error),
                    caption = resources.getString(R.string.schedule_week_loaded_error_caption)
                )
            }
            Resource.SCHEDULE_SUBJECT_EDITED -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_subject_edited),
                    caption = resources.getString(R.string.schedule_subject_edited_caption)
                )
            }
            Resource.SCHEDULE_SUBJECT_DELETED -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_subject_deleted),
                    caption = resources.getString(R.string.schedule_subject_deleted_caption)
                )
            }
            Resource.SCHEDULE_SUBJECT_IGNORED -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_subject_ignored),
                    caption = resources.getString(R.string.schedule_subject_ignored_caption)
                )
            }
            Resource.SCHEDULE_SUBJECT_NOT_IGNORED -> {
                ErrorMessageText(
                    title = resources.getString(R.string.schedule_subject_not_ignored),
                    caption = resources.getString(R.string.schedule_subject_not_ignored_caption)
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


