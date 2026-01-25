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
}