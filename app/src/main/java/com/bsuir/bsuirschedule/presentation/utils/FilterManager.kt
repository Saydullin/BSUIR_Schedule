package com.bsuir.bsuirschedule.presentation.utils

import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import com.bsuir.bsuirschedule.databinding.FilterBinding

class FilterManager(
    private val filter: FilterBinding,
    private val filterCallback: (s: String, isAsc: Boolean) -> Unit,
    defaultIsAscSort: Boolean? = null
): Application() {
    private var isAscSort = defaultIsAscSort ?: (filter.filterSort.rotation == 0f)

    fun init() {

        filter.editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                filterCallback(p0.toString().trim(), isAscSort)
            }
        })

        filterSortRotate(isAscSort)

        filter.filterSort.setOnClickListener {
            isAscSort = !isAscSort
            filterSortRotate(isAscSort)
            filterCallback(filter.editText.text.toString().trim(), isAscSort)
        }

        filter.editText.isSaveEnabled = false
    }

    private fun filterSortRotate(isAsc: Boolean) {
        filter.filterSort.rotation = if (isAsc) 180f else 0f
    }

}


