package com.example.sportapp.ui.screens

import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportapp.data.model.ArticleDto
import com.example.sportapp.ui.favorites.FavoritesViewModel
import com.example.sportapp.ui.components.ArticleCard

@Composable
fun FavoritesScreen() {
    val vm: FavoritesViewModel = viewModel()
    val savedIds by vm.saveIds.collectAsState()
    // on réutilise observeAll() via un State (petit helper ci-dessous)
    val savedArticles by rememberSavedArticles(vm)

    if (savedArticles.isEmpty()) {
        Box(Modifier.fillMaxSize().padding(16.dp)) {
            Text("Aucun favori pour le moment.", style = MaterialTheme.typography.bodyMedium)
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(savedArticles, key = { it.id }) { article ->
            ArticleCard(
                article = article,
                saved = savedIds.contains(article.id),
                onToggleSave = { vm.toggle(it) },
                onClick = null,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun rememberSavedArticles(vm: FavoritesViewModel): State<List<com.example.sportapp.data.model.ArticleDto>> {
    // petit collectAsState “manuel” pour Flow<List<ArticleDto>>
    val state = remember { mutableStateOf(emptyList<com.example.sportapp.data.model.ArticleDto>()) }
    LaunchedEffect(Unit) {
        vm.saveIds // ensure VM stays alive
    }
    // On pourrait exposer un flow dans le VM, ici on fait simple :
    val ctx = androidx.compose.ui.platform.LocalContext.current.applicationContext
    val repo = remember(ctx) { com.example.sportapp.data.repo.FavoritesRepository(ctx) }
    LaunchedEffect(repo) {
        repo.observeAll().collect { state.value = it }
    }
    return state
}

@Composable
private fun FavoritesScreenContentPreview(articles: List<com.example.sportapp.data.model.ArticleDto>) {
    if (articles.isEmpty()) {
        androidx.compose.foundation.layout.Box(Modifier.fillMaxSize().padding(16.dp)) {
            Text("Aucun favori pour le moment.", style = MaterialTheme.typography.bodyMedium)
        }
        return
    }
    androidx.compose.foundation.lazy.LazyColumn(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(articles, key = { it.id }) { a ->
            Text("★ ${a.title}")
        }
    }
}

@Preview(name = "Favorites - Empty", showBackground = true)
@Composable
private fun Preview_Favorites_Empty() {
    FavoritesScreenContentPreview(emptyList())
}

@Preview(name = "Favorites - List", showBackground = true)
@Composable
private fun Preview_Favorites_List() {
    val fake = List(2) {
        ArticleDto(
            id = "fav$it",
            title = "Favori $it",
            category = "Tech",
            summary = "Résumé…",
            imageUrl = null, publishedAt = "", source = "The Guardian", url = "https://example.com"
        )
    }
    FavoritesScreenContentPreview(fake)
}
