package com.example.sportapp.data.repo

import android.content.Context
import com.example.sportapp.data.local.AppDatabase
import com.example.sportapp.data.local.SavedArticleEntity
import com.example.sportapp.data.model.ArticleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepository(ctx: Context) {
    private val dao = AppDatabase.get(ctx).savedArticleDao()

    fun observeSavedIds(): Flow<Set<String>> =
        dao.observeAllIds().map { it.toSet() }

    fun observeAll(): Flow<List<ArticleDto>> =
        dao.observeAll().map { list ->
            list.map {
                ArticleDto(
                    id = it.id,
                    title = it.title,
                    category = "Favorite",
                    summary = it.summary,
                    imageUrl = it.imageUrl,
                    publishedAt = "",   // Non stocké
                    source = "Saved",
                    url = ""            // Non stocké
                )
            }
        }

    suspend fun save(article: ArticleDto) {
        dao.upsert(
            SavedArticleEntity(
                id = article.id,
                title = article.title,
                summary = article.summary,
                imageUrl = article.imageUrl,
                savedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun remove(id: String) = dao.deleteById(id)

    suspend fun isSaved(id: String): Boolean = dao.exists(id) > 0
}