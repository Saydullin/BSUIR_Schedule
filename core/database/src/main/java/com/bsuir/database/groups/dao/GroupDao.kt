package com.bsuir.database.groups.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.bsuir.database.groups.entity.GroupEntity

@Dao
interface GroupDao {

    @Query("SELECT * FROM `group` ORDER BY name ASC")
    fun getAllGroups(): List<GroupEntity>

    @Query("SELECT * FROM `group` WHERE name LIKE :searchLike ORDER BY name ASC LIMIT :limit OFFSET :offset")
    fun getPagingGroups(
        searchLike: String,
        limit: Int,
        offset: Int
    ): List<GroupEntity>

    @Query("SELECT * FROM `group` WHERE id = :id")
    fun getById(id: Long): GroupEntity?

    @Query("SELECT * FROM `group` WHERE name = :name")
    fun getByName(name: String): GroupEntity?

    @Query("SELECT * FROM `group` WHERE name LIKE :name")
    fun getListByName(name: String): List<GroupEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(groups: List<GroupEntity>): List<Long>

    @Query("DELETE FROM `group`")
    fun clear(): Int

    @Transaction
    fun clearAndSave(groups: List<GroupEntity>) {
        clear()
        save(groups)
    }

}