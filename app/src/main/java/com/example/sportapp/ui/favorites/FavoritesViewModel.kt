package com.example.sportapp.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportapp.data.model.ArticleDto
import com.example.sportapp.data.repo.FavoritesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = FavoritesRepository(app)

    // Set d'IDs sauvé pour marquer/afficher l'état "bookmark"
    val saveIds: StateFlow<Set<String>> =
        repo.observeSavedIds()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    fun toggle(article: ArticleDto) {
        viewModelScope.launch {
            val isSaved = repo.isSaved(article.id)
            if (isSaved) repo.remove(article.id) else repo.save(article)
        }
    }
}