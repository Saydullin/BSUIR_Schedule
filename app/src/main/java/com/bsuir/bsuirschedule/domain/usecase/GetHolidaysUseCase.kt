package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.Holiday
import com.bsuir.bsuirschedule.domain.repository.HolidayRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.StatusCode
import java.util.Calendar
import java.util.Date

class GetHolidaysUseCase(
    private val holidayRepository: HolidayRepository,
) {

    suspend fun execute(): Resource<List<Holiday>> {
        return try {
            val holidays = holidayRepository.getHolidays()

            if (holidays is Resource.Error || holidays.data == null) {
                return Resource.Error(
                    statusCode = holidays.statusCode,
                    message = holidays.message
                )
            }

            val easterHolidayDate = getOrthodoxEasterHolidayDate()
            val easterHoliday = Holiday(
                id = holidays.data.size + 1,
                date = easterHolidayDate.time,
                title = "Пасха"
            )

            val holidaysList = mutableListOf<Holiday>()
            holidaysList.addAll(holidays.data)
            holidaysList.add(easterHoliday)

            Resource.Success(holidaysList)
        } catch (e: Exception) {
            Resource.Error(
                statusCode = StatusCode.DATA_ERROR
            )
        }
    }

    private fun getOrthodoxEasterHolidayDate(): Date {
        val currentCalendar = Calendar.getInstance()
        val year = currentCalendar.get(Calendar.YEAR)
        val a = year % 19
        val b = year % 4
        val c = year % 7
        val d = (19 * a + 15) % 30
        val e = (2 * b + 4 * c - 6 * d + 6) % 7
        val f = d + e
        val month = if (f <= 26) 4 else 5
        val date = if (f <= 26) 4 + f else f - 26

        currentCalendar.set(1970, month, date)

        return currentCalendar.time
    }

}