package com.example.sportapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedArticleDao {
    @Query("SELECT * FROM saved_articles ORDER BY savedAt DESC")
    fun observeAll(): Flow<List<SavedArticleEntity>>

    @Query("SELECT id FROM saved_articles")
    fun observeAllIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: SavedArticleEntity)

    @Delete
    suspend fun delete(entity: SavedArticleEntity)

    @Query("DELETE FROM saved_articles WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM saved_articles WHERE id = :id")
    suspend fun exists(id: String): Int
}