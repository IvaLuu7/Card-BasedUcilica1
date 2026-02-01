package com.example.card_baseducilica.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.card_baseducilica.data.entity.SetEntity

@Dao
interface SetDao {

    @Insert
    suspend fun insert(set: SetEntity)

    @Query("SELECT * FROM sets WHERE userId = :userId")
    suspend fun getSetsForUser(userId: Int): List<SetEntity>

    @Query("DELETE FROM sets WHERE id = :setId")
    suspend fun deleteById(setId: Int)

    @Insert
    suspend fun insertSetAndReturnId(set: SetEntity): Long

    @Query("UPDATE sets SET title = :title, description = :description WHERE id = :setId")
    suspend fun updateSet(setId: Int, title: String, description: String)

}