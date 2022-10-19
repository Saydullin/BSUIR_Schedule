package com.bsuir.bsuirschedule.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bsuir.bsuirschedule.data.db.entities.GroupTable

@Dao
interface GroupDao {

    @Query("SELECT * FROM GroupTable ORDER BY name ASC")
    fun getAllGroups(): List<GroupTable>

    @Query("SELECT * FROM GroupTable WHERE name LIKE :s " +
            "OR faculty_abbr||' '||speciality_education_form_name LIKE :s " +
            "OR faculty_abbr||' '||course LIKE :s " +
            "OR faculty_name LIKE :s " +
            "OR speciality_name LIKE :s " +
            "OR faculty_abbr||' '||speciality_abbrev||' '||course||' '||speciality_education_form_name LIKE :s  ORDER BY name ASC")
    fun filterByKeywordASC(s: String): List<GroupTable>

    @Query("SELECT * FROM GroupTable WHERE name LIKE :s " +
            "OR faculty_abbr||' '||speciality_abbrev||' '||course||' '||speciality_education_form_name LIKE :s " +
            "OR faculty_abbr||' '||speciality_education_form_name LIKE :s " +
            "OR faculty_abbr||' '||course LIKE :s " +
            "OR faculty_name LIKE :s " +
            "OR speciality_name LIKE :s ORDER BY name DESC")
    fun filterByKeywordDESC(s: String): List<GroupTable>

    @Query("SELECT * FROM GroupTable WHERE course=:course ORDER BY name ASC")
    fun filterByCourseASC(course: Int): List<GroupTable>

    @Query("SELECT * FROM GroupTable WHERE course=:course ORDER BY name DESC")
    fun filterByCourseDESC(course: Int): List<GroupTable>

    @Query("SELECT * FROM GroupTable WHERE name = :name")
    fun getGroupByName(name: String): GroupTable

    @Query("SELECT * FROM GroupTable WHERE facultyId = :facultyId")
    fun getGroupsByFaculty(facultyId: Int): List<GroupTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllGroups(groups: List<GroupTable>)

    @Query("SELECT * FROM GroupTable WHERE speciality_id = :depId")
    fun getGroupsByDepartment(depId: Int): List<GroupTable>

    @Query("SELECT * FROM GroupTable WHERE course = :course")
    fun getGroupsByCourse(course: Int): List<GroupTable>

}


