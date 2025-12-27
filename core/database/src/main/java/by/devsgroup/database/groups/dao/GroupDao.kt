package by.devsgroup.database.groups.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.devsgroup.database.groups.entity.GroupEntity

@Dao
interface GroupDao {

    @Query("SELECT * FROM `group` ORDER BY name ASC")
    fun getAllGroups(): List<GroupEntity>

    @Query("SELECT * FROM `group` ORDER BY name ASC LIMIT :limit OFFSET :offset")
    fun getPagingGroups(limit: Int, offset: Int): List<GroupEntity>

    @Query("SELECT * FROM `group` WHERE id = :id")
    fun getById(id: Int): GroupEntity?

    @Query("SELECT * FROM `group` WHERE name = :name")
    fun getByName(name: String): GroupEntity?

    @Query("SELECT * FROM `group` WHERE name LIKE :name")
    fun getListByName(name: String): List<GroupEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(groups: List<GroupEntity>): List<Long>

    @Query("DELETE FROM `group`")
    fun clear(): Int

}