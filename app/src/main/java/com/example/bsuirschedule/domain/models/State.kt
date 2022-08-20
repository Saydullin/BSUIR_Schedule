package com.example.bsuirschedule.domain.models

data class State(
    var isOk: Boolean,
    var message: String,
) {

    companion object {
        val default = State(
            false,
            ""
        )
    }

}


