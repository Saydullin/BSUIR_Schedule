package com.bsuir.bsuirschedule.presentation.utils

import android.content.Context
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FilterBinding

class FilterManager(
    private val context: Context,
    private val filter: FilterBinding,
    private val filterCallback: (s: String, isAsc: Boolean) -> Unit,
    defaultIsAscSort: Boolean? = null
) {
    private var isAscSort = defaultIsAscSort ?: (filter.filterSort.rotation == 0f)

    fun init() {

        val editTextHint = context.getString(R.string.search_placeholder)

        with(filter.editText) {
            setHint(editTextHint)

            setTextChangeListener {
                filterCallback(it, isAscSort)
            }
        }

        filterSortRotate(isAscSort)

        filter.filterSort.setOnClickListener {
            isAscSort = !isAscSort
            filterSortRotate(isAscSort)
            filterCallback(filter.editText.getText(), isAscSort)
        }

        filter.editText.isSaveEnabled = false
    }

    private fun filterSortRotate(isAsc: Boolean) {
        filter.filterSort.rotation = if (isAsc) 180f else 0f
    }

}


