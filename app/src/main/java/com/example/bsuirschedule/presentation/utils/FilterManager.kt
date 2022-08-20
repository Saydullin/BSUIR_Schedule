package com.example.bsuirschedule.presentation.utils

import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import com.example.bsuirschedule.databinding.FilterBinding

class FilterManager(
    private val filter: FilterBinding,
    private val filterCallback: (s: String, isAsc: Boolean) -> Unit,
    defaultIsAscSort: Boolean = true
): Application() {
    private var isAscSort = defaultIsAscSort

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

        filter.filterSort.setOnClickListener {
            isAscSort = !isAscSort
            filterCallback(filter.editText.text.toString().trim(), isAscSort)
        }

        filter.editText.isSaveEnabled = false
    }

}


