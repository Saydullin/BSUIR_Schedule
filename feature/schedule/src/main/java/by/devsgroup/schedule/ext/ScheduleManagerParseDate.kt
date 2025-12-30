package by.devsgroup.schedule.ext

import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal fun parseDate(date: String): LocalDate? {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    return try {
        LocalDate.parse(date, formatter)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}