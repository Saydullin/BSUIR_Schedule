package by.devsgroup.schedule.ext

import by.devsgroup.schedule.ui.model.PreviewScheduleType

fun PreviewScheduleType.Employee.fullName(): String {
    return listOfNotNull(lastName, firstName, middleName)
        .joinToString(" ")
        .trim()
}


