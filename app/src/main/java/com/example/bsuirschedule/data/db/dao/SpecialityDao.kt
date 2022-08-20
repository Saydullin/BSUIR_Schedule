package com.example.bsuirschedule.data.db.dao

import androidx.room.*
import com.example.bsuirschedule.data.db.entities.SpecialityTable

@Dao
interface SpecialityDao {

    @Query("SELECT * FROM SpecialityTable ORDER BY abbrev")
    fun getSpecialities(): List<SpecialityTable>

    @Query("SELECT * FROM SpecialityTable WHERE id=:id")
    fun getSpecialitiesById(id: Int): SpecialityTable

    @Query("SELECT * FROM SpecialityTable WHERE name=:name")
    fun getSpecialityAbbrByName(name: String): SpecialityTable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSpecialities(specialities: List<SpecialityTable>)

}


