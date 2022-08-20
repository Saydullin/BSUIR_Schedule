package com.example.bsuirschedule.domain.models

data class StateStatus(
    val state: Int,
    val type: Int,
    val message: String? = ""
) {

    companion object {
        const val SUCCESS_STATE = 1
        const val ERROR_STATE = 2
        const val INFO_STATE = 3
    }

}


