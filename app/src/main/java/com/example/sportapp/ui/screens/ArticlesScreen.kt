// ui/screens/ArticlesScreen.kt
package com.example.sportapp.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.parser.Section
import com.example.sportapp.data.model.ArticleDto
import com.example.sportapp.ui.articles.ArticlesViewModel
import com.example.sportapp.ui.articles.SectionFilter
import com.example.sportapp.ui.components.ArticleCard
import com.example.sportapp.ui.favorites.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesScreen() {
    val vm: ArticlesViewModel = viewModel()
    val state by vm.state
    val context = LocalContext.current
    val favVm: FavoritesViewModel = viewModel()
    val savedIds by favVm.saveIds.collectAsState()

    Column(Modifier.fillMaxSize()) {
        // ---- NEW: Section chips bar ----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.sections.forEach { section ->
                val selected = section == state.selectedSection
                FilterChip(
                    selected = selected,
                    onClick = { vm.setSection(section) },
                    label = { Text(section.label) }
                )
            }
        }

        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { vm.refresh() },
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                state.isLoading -> {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(Modifier.padding(24.dp))
                    }
                }

                state.error != null -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Error: ${state.error}")
                        Button(onClick = { vm.load() }) { Text("Try Again") }
                    }
                }

                else -> {
                    val visible = state.articles.take(state.visibleCount)

                    if (visible.isEmpty()) {
                        Box(Modifier.fillMaxSize().padding(16.dp)) {
                            Text("Aucun article à afficher")
                        }
                        return@PullToRefreshBox
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(
                            items = visible,
                            key = { _, a -> a.id }
                        ) { index, article ->
                            ArticleCard(
                                article = article,
                                saved = savedIds.contains(article.id),
                                onToggleSave = { favVm.toggle(it)},
                                modifier = Modifier.fillMaxWidth(),
                                onClick = null,
                            )

                            if (index == visible.lastIndex) {
                                LaunchedEffect(visible.size) { vm.loadMore() }
                            }
                        }

                        if (state.visibleCount < state.articles.size) {
                            item {
                                Box(Modifier.fillMaxWidth().padding(16.dp)) {
                                    LinearProgressIndicator(Modifier.fillMaxWidth())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class PreviewArticlesState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val sections: List<SectionFilter> = emptyList(),
    val selectedSection: SectionFilter = SectionFilter("football", "Football"),
    val articles: List<ArticleDto> = emptyList(),
    val visibleCount: Int = articles.size
)

@Composable
private fun ArticlesScreenContentPreview(state: PreviewArticlesState) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Prévisualisation Articles (${state.selectedSection.label})")
        when {
            state.isLoading -> CircularProgressIndicator()
            state.error != null -> Text("Error: ${state.error}")
            else -> {
                state.articles.take(state.visibleCount).forEach {
                    Text("• ${it.title}")
                }
                if (state.articles.isEmpty()) Text("Aucun article à afficher")
            }
        }
    }
}

@Preview(name = "Articles - Light", showBackground = true)
@Preview(name = "Articles - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Preview_ArticlesScreen() {
    val fake = List(3) {
        ArticleDto(
            id = "id$it",
            title = "Article $it de démo",
            category = "Football",
            summary = "Résumé $it ...",
            imageUrl = null, publishedAt = "", source = "The Guardian", url = "https://example.com"
        )
    }
    ArticlesScreenContentPreview(
        PreviewArticlesState(
            sections = listOf(
                SectionFilter("football","Football"),
                SectionFilter("sport","Sport")
            ),
            articles = fake
        )
    )
}
