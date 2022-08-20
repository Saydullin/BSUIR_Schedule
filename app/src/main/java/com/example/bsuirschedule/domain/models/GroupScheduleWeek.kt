package com.example.bsuirschedule.domain.models

import com.example.bsuirschedule.data.db.entities.GroupScheduleSubjectTable
import com.example.bsuirschedule.data.db.entities.GroupScheduleWeekTable
import com.google.gson.annotations.SerializedName

data class GroupScheduleWeek (
    @SerializedName("Понедельник") var monday: ArrayList<ScheduleSubject>? = ArrayList(),
    @SerializedName("Вторник") var tuesday: ArrayList<ScheduleSubject>? = ArrayList(),
    @SerializedName("Среда") var wednesday: ArrayList<ScheduleSubject>? = ArrayList(),
    @SerializedName("Четверг") var thursday: ArrayList<ScheduleSubject>? = ArrayList(),
    @SerializedName("Пятница") var friday: ArrayList<ScheduleSubject>? = ArrayList(),
    @SerializedName("Суббота") var saturday: ArrayList<ScheduleSubject>? = ArrayList(),
) {

    companion object {
        val empty = GroupScheduleWeek(
            monday = ArrayList(),
            tuesday = ArrayList(),
            wednesday = ArrayList(),
            thursday = ArrayList(),
            friday = ArrayList(),
            saturday = ArrayList(),
        )
    }

    fun toGroupScheduleWeekTable() = GroupScheduleWeekTable(
        monday = monday?.map { it.toGroupScheduleSubjectTable() } as ArrayList<GroupScheduleSubjectTable>,
        tuesday = tuesday?.map { it.toGroupScheduleSubjectTable() } as ArrayList<GroupScheduleSubjectTable>,
        wednesday = wednesday?.map { it.toGroupScheduleSubjectTable() } as ArrayList<GroupScheduleSubjectTable>,
        thursday = thursday?.map { it.toGroupScheduleSubjectTable() } as ArrayList<GroupScheduleSubjectTable>,
        friday = friday?.map { it.toGroupScheduleSubjectTable() } as ArrayList<GroupScheduleSubjectTable>,
        saturday = saturday?.map { it.toGroupScheduleSubjectTable() } as ArrayList<GroupScheduleSubjectTable>
    )

}


