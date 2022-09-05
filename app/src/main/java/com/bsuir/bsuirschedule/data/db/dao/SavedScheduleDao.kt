package com.bsuir.bsuirschedule.data.db.dao

import androidx.room.*
import com.bsuir.bsuirschedule.data.db.entities.SavedScheduleTable

@Dao
interface SavedScheduleDao {

    @Query("SELECT * FROM SavedScheduleTable ORDER BY lastUpdateTime DESC")
    fun getSavedSchedules(): List<SavedScheduleTable>

    @Query("SELECT * FROM SavedScheduleTable WHERE " +
            "employee_fullName LIKE :title " +
            "OR employee_degreeAbbrev LIKE :title " +
            "OR employee_degree LIKE :title " +
            "OR employee_rank LIKE :title " +
            "OR employee_academicDepartment LIKE :title " +
            "OR group_name LIKE :title " +
            "OR group_speciality_abbrev LIKE :title " +
            "OR group_speciality_name LIKE :title " +
            "OR group_faculty_abbr LIKE :title " +
            "OR group_faculty_abbr LIKE :title " +
            "OR group_speciality_education_form_name LIKE :title " +
            "ORDER BY lastUpdateTime ASC")
    fun filterByKeywordASC(title: String): List<SavedScheduleTable>

    @Query("SELECT * FROM SavedScheduleTable WHERE " +
            "employee_fullName LIKE :title " +
            "OR employee_degreeAbbrev LIKE :title " +
            "OR employee_degree LIKE :title " +
            "OR employee_rank LIKE :title " +
            "OR employee_academicDepartment LIKE :title " +
            "OR group_name LIKE :title " +
            "OR group_speciality_abbrev LIKE :title " +
            "OR group_speciality_name LIKE :title " +
            "OR group_faculty_abbr LIKE :title " +
            "OR group_faculty_abbr LIKE :title " +
            "OR group_speciality_education_form_name LIKE :title " +
            "ORDER BY lastUpdateTime DESC")
    fun filterByKeywordDESC(title: String): List<SavedScheduleTable>

    @Query("SELECT * FROM SavedScheduleTable WHERE id=:scheduleId")
    fun getSavedScheduleById(scheduleId: Int): SavedScheduleTable?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSchedule(schedule: SavedScheduleTable)

    @Delete
    fun deleteSchedule(schedule: SavedScheduleTable)

}


