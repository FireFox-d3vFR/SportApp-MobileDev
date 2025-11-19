package com.example.sportapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("saved_articles")
data class SavedArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val imageUrl: String?,
    val savedAt: Long
)