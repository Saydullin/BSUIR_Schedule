package com.bsuir.schedule.ext

import androidx.compose.ui.graphics.Color
import com.bsuir.domain.model.schedule.full.FullScheduleLesson

fun FullScheduleLesson.lessonTypeColorParse(): Color {
    return when(this.lessonTypeAbbrev) {
        "ПЗ" -> Color.Yellow.copy(.3f)
        "ЛК" -> Color.Green.copy(.2f)
        "ЛР" -> Color.Red.copy(.2f)
        else -> Color.Gray.copy(.2f)
    }
}


