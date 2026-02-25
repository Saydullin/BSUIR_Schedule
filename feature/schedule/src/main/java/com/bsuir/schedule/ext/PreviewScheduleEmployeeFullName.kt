package com.bsuir.schedule.ext

import com.bsuir.schedule.ui.model.PreviewScheduleType

fun PreviewScheduleType.Employee.fullName(): String {
    return listOfNotNull(lastName, firstName, middleName)
        .joinToString(" ")
        .trim()
}


