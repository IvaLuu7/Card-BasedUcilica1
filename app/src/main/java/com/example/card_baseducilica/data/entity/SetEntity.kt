package com.example.card_baseducilica.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sets")
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val title: String,
    val description: String?
)