package com.example.bsuirschedule.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bsuirschedule.domain.models.EducationForm

@Entity
data class EducationFormTable(
    @PrimaryKey(autoGenerate = true) val efId: Int = 0,
    @ColumnInfo val id: Int,
    @ColumnInfo val name: String
) {

    fun toEducationForm() = EducationForm(
        id = id,
        name = name
    )

}

