// ui/articles/ArticlesViewModel.kt
package com.example.sportapp.ui.articles

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportapp.data.model.ArticleDto
import com.example.sportapp.data.repo.ArticleRepository
import com.example.sportapp.data.settings.AppPreferences
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.min

data class ArticlesUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val articles: List<ArticleDto> = emptyList(),
    val visibleCount: Int = 10,
    val error: String? = null,

    // --- NEW: section filter ---
    val sections: List<SectionFilter> = defaultSections,
    val selectedSection: SectionFilter = defaultSections.first()
)

data class SectionFilter(val key: String, val label: String)

// Guardian sections you want to expose (you can change/reorder later)
private val defaultSections = listOf(
    SectionFilter(key = "football",   label = "Football"),
    SectionFilter(key = "sport",      label = "Sport"),
    SectionFilter(key = "world",      label = "World"),
    SectionFilter(key = "uk-news",    label = "UK"),
    SectionFilter(key = "technology", label = "Tech"),
    SectionFilter(key = "culture",    label = "Culture"),
)

class ArticlesViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ArticleRepository()
    private val prefs = AppPreferences(app)

    private val _state = mutableStateOf(ArticlesUiState())
    val state: State<ArticlesUiState> = _state

    init {
        // Charge la liste pour la section par défaut
        load()

        // Lire la pref et applique si elle change
        viewModelScope.launch {
            prefs.prefsFlow
                .map { it.defaultSectionId }
                .distinctUntilChanged()
                .collect { id ->
                    // Si la pref est différente de la section affichée
                    if (id != null && id != _state.value.selectedSection.key) {
                        setSectionById(id, persist = false)
                    }
                }
        }
    }

    fun setSection(section: SectionFilter, persist: Boolean = true) {
        if (section == _state.value.selectedSection) return
        _state.value = _state.value.copy(selectedSection = section)
        if (persist) {
            // on mémorise la sélection par défaut
            viewModelScope.launch { prefs.setDefaultSection(section.key) }
        }
        load() // rechargement pour la nouvelle section
    }

    private fun setSectionById(id: String, persist: Boolean = false) {
        val match = _state.value.sections.find { it.key == id } ?: return
        setSection(match, persist = persist)
    }

    fun load() {
        val section = _state.value.selectedSection.key
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val list = repo.fetchArticles(category = section)
                _state.value = _state.value.copy(
                    isLoading = false,
                    articles = list,
                    visibleCount = min(10, list.size)
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erreur de chargement"
                )
            }
        }
    }

    fun refresh() {
        val section = _state.value.selectedSection.key
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true, error = null)
            try {
                val list = repo.fetchArticles(category = section)
                _state.value = _state.value.copy(
                    isRefreshing = false,
                    articles = list,
                    visibleCount = min(10, list.size)
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isRefreshing = false,
                    error = e.message ?: "Erreur lors du rafraîchissement"
                )
            }
        }
    }

    fun loadMore() {
        val s = _state.value
        if (s.visibleCount < s.articles.size) {
            _state.value = s.copy(
                visibleCount = min(s.visibleCount + 10, s.articles.size)
            )
        }
    }
}
