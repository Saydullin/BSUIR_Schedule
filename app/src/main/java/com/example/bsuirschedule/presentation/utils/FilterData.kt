package com.example.bsuirschedule.presentation.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class FilterData {

    fun setTextListener(editText: EditText, filterCallback: (s: String) -> Unit) {
        editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                filterCallback(p0.toString())
            }
        })
    }

}


