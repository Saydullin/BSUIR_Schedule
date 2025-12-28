package by.devsgroup.database.specialty.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.devsgroup.database.specialty.entity.SpecialtyEntity

@Dao
interface SpecialtyDao {

    @Query("SELECT * FROM `specialty` ORDER BY `name` ASC")
    fun getAllSpecialties(): List<SpecialtyEntity>

    @Query("SELECT * FROM `specialty` WHERE id = :id")
    fun getById(id: Long): SpecialtyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(faculties: List<SpecialtyEntity>): List<Long>

    @Query("DELETE FROM `specialty`")
    fun clear(): Int

}