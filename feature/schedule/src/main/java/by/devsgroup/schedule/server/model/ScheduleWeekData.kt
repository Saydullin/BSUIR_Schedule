package by.devsgroup.schedule.server.model

import com.google.gson.annotations.SerializedName

data class ScheduleWeekData(
    @SerializedName("Понедельник")
    val monday: List<ScheduleLessonData>?,

    @SerializedName("Вторник")
    val tuesday: List<ScheduleLessonData>?,

    @SerializedName("Среда")
    val wednesday: List<ScheduleLessonData>?,

    @SerializedName("Четверг")
    val thursday: List<ScheduleLessonData>?,

    @SerializedName("Пятница")
    val friday: List<ScheduleLessonData>?,

    @SerializedName("Суббота")
    val saturday: List<ScheduleLessonData>?
)
