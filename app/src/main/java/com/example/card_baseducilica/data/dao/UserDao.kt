package com.example.card_baseducilica.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.card_baseducilica.data.entity.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): UserEntity?
}