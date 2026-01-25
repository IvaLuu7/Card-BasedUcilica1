package com.example.card_baseducilica.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.card_baseducilica.data.entity.CardEntity

@Dao
interface CardDao {

    @Insert
    suspend fun insert(card: CardEntity)

    @Query("SELECT * FROM cards WHERE setId = :setId")
    suspend fun getCardsForSet(setId: Int): List<CardEntity>

    @Query("DELETE FROM cards WHERE id = :cardId")
    suspend fun deleteById(cardId: Int)
}