package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.GroupScheduleWeek

@Entity
data class GroupScheduleWeekTable (
    @PrimaryKey(autoGenerate = true) var id: Int = -1,
    @Embedded val monday: ArrayList<GroupScheduleSubjectTable>,
    @Embedded val tuesday: ArrayList<GroupScheduleSubjectTable>,
    @Embedded val wednesday: ArrayList<GroupScheduleSubjectTable>,
    @Embedded val thursday: ArrayList<GroupScheduleSubjectTable>,
    @Embedded val friday: ArrayList<GroupScheduleSubjectTable>,
    @Embedded val saturday: ArrayList<GroupScheduleSubjectTable>,
) {

    fun toGroupScheduleWeek() = GroupScheduleWeek(
        monday.map { it.toScheduleSubject() } as ArrayList<ScheduleSubject>,
        tuesday.map { it.toScheduleSubject() } as ArrayList<ScheduleSubject>,
        wednesday.map { it.toScheduleSubject() } as ArrayList<ScheduleSubject>,
        thursday.map { it.toScheduleSubject() } as ArrayList<ScheduleSubject>,
        friday.map { it.toScheduleSubject() } as ArrayList<ScheduleSubject>,
        saturday.map { it.toScheduleSubject() } as ArrayList<ScheduleSubject>
    )

}


