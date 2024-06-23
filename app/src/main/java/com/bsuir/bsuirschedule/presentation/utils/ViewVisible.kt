package com.bsuir.bsuirschedule.presentation.utils

import android.view.View

class ViewVisible {

    companion object {
        fun ifIs(isVisible: Boolean): Int {
            return if (isVisible) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

}