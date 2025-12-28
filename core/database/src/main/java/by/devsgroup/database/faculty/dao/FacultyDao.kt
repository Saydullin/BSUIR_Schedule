package by.devsgroup.database.faculty.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.devsgroup.database.faculty.entity.FacultyEntity

@Dao
interface FacultyDao {

    @Query("SELECT * FROM `faculty` ORDER BY `name` ASC")
    fun getAllFaculties(): List<FacultyEntity>

    @Query("SELECT * FROM `faculty` WHERE id = :id")
    fun getById(id: Long): FacultyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(faculties: List<FacultyEntity>): List<Long>

    @Query("DELETE FROM `faculty`")
    fun clear(): Int

}