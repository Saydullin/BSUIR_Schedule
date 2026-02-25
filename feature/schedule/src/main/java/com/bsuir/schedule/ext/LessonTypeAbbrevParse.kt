package com.bsuir.schedule.ext

import com.bsuir.domain.model.schedule.full.FullScheduleLesson

fun FullScheduleLesson.lessonTypeAbbrevParse(): String {
    return when(this.lessonTypeAbbrev) {
        "ПЗ" -> "практика"
        "ЛК" -> "лекция"
        "ЛР" -> "лабораторная"
        else -> this.lessonTypeAbbrev ?: "неизвестно"
    }
}


