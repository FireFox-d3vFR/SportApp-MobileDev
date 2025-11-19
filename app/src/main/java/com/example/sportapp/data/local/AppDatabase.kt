package com.example.sportapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SavedArticleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedArticleDao(): SavedArticleDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(ctx: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    ctx.applicationContext,
                    AppDatabase::class.java,
                    "sportapp.db"
                ).build().also { INSTANCE = it }
            }
    }
}