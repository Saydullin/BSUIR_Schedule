package by.devsgroup.schedule.ext

import by.devsgroup.domain.model.schedule.full.FullScheduleLesson

fun FullScheduleLesson.lessonTypeAbbrevParse(): String {
    return when(this.lessonTypeAbbrev) {
        "ПЗ" -> "практика"
        "ЛК" -> "лекция"
        "ЛР" -> "лабораторная"
        else -> this.lessonTypeAbbrev ?: "неизвестно"
    }
}


