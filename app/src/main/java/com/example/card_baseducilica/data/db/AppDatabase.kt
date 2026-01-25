package com.example.card_baseducilica.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.card_baseducilica.data.dao.CardDao
import com.example.card_baseducilica.data.dao.SetDao
import com.example.card_baseducilica.data.dao.UserDao
import com.example.card_baseducilica.data.entity.CardEntity
import com.example.card_baseducilica.data.entity.SetEntity
import com.example.card_baseducilica.data.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        SetEntity::class,
        CardEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun setDao(): SetDao
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "card_based_ucilica_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}