package com.example.sportapp.ui.screens

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.sportapp.data.model.ArticleDto
import com.example.sportapp.notifications.NotificationHelper
import com.example.sportapp.ui.articles.ArticlesViewModel
import com.example.sportapp.ui.components.quick
import com.example.sportapp.ui.theme.SportAppTheme
import com.example.sportapp.util.openCustomTab
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenArticles: (() -> Unit)? = null,
    onOpenFavorites: (() -> Unit)? = null,
    onOpenSettings: (() -> Unit)? = null
) {
    val vm: ArticlesViewModel = viewModel()
    val state by vm.state
    val ctx = LocalContext.current
    val snackbarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { vm.refresh() },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(text = "SportApp", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "Toute l’actu sport, en un coup d’œil.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            SnackbarHost(hostState = snackbarHost)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    Toast.makeText(ctx, "Hello from Toast!", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(Icons.Default.Message, contentDescription = null)
                    Spacer(Modifier.width(8.dp)); Text("Toast")
                }
                Button(onClick = {
                    scope.launch { snackbarHost.quick("Ceci est une Snackbar", "OK") }
                }) {
                    Icon(Icons.Default.Info, contentDescription = null)
                    Spacer(Modifier.width(8.dp)); Text("Snackbar")
                }
                Button(onClick = {
                    NotificationHelper.show(ctx, "SportApp", "Notification de test ✅")
                }) {
                    Icon(Icons.Default.Notifications, contentDescription = null)
                    Spacer(Modifier.width(8.dp)); Text("Notification")
                }
            }

            // Actions rapides
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                QuickActionCard(
                    title = "Articles",
                    icon = Icons.Filled.SportsSoccer,
                    modifier = Modifier.weight(1f)
                ) { onOpenArticles?.invoke() }

                QuickActionCard(
                    title = "Favoris",
                    icon = Icons.Filled.Bookmarks,
                    modifier = Modifier.weight(1f)
                ) { onOpenFavorites?.invoke() }

                QuickActionCard(
                    title = "Réglages",
                    icon = Icons.Filled.Settings,
                    modifier = Modifier.weight(1f)
                ) { onOpenSettings?.invoke() }
            }

            // Filtres section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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

            // À la une
            Text(
                text = "À la une • ${state.selectedSection.label}",
                style = MaterialTheme.typography.titleMedium
            )

            val featured = state.articles.take(10)
            if (featured.isEmpty() && !state.isLoading) {
                EmptyBox("Aucun article disponible pour cette section.")
            } else {
                LazyRow(
                    contentPadding = PaddingValues(end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(featured, key = { it.id }) { article ->
                        FeaturedArticleCard(
                            article = article,
                            onClick = { openCustomTab(ctx, article.url) },
                            modifier = Modifier.fillMaxWidth(0.88f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Tip
            TipCard(
                text = "Astuce: change la section au-dessus (Football, Sport…) pour filtrer le flux. " +
                        "Tape un article pour l’ouvrir dans un onglet sécurisé."
            )
        }
    }
}

@Composable
private fun FeaturedArticleCard(
    article: ArticleDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = article.title,
                loading = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                },
                error = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(MaterialTheme.shapes.medium)
            )

            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = article.category,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedCard(onClick = onClick, modifier = modifier) {
        Column(
            Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null)
            Text(title, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun TipCard(text: String) {
    Card {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
private fun EmptyBox(message: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/** @Preview **/
@Preview(showBackground = true, name = "Tip - Light")
@Preview(
    showBackground = true,
    name = "Tip - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TipCardPreview() {
    SportAppTheme(themeMode = com.example.sportapp.data.settings.ThemeMode.LIGHT) {
        TipCard("Aperçu d'une astuce")
    }
}

@Preview(showBackground = true, name = "Featured - Phone")
@Composable
private fun FeaturedCardPreview() {
    val fake = ArticleDto(
        id = "id",
        title = "Titre d'exemple très long pour tester l'ellispe...",
        category = "Football",
        summary = "Résumé court d'exemple...",
        imageUrl = null,
        publishedAt = "",
        source = "The Guardian",
        url = "https://example.com"
    )
    SportAppTheme(themeMode = com.example.sportapp.data.settings.ThemeMode.LIGHT) {
        FeaturedArticleCard(article = fake, onClick = {})
    }
}

@Preview(name = "Home - Light", showBackground = true)
@Preview(name = "Home - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Preview_HomeScreen() {
    val fake = List(5) {
        ArticleDto(
            id = "id$it",
            title = "Titre $it très très long pour tester…",
            category = if (it % 2 == 0) "Football" else "Sport",
            summary = "Résumé court $it",
            imageUrl = null, publishedAt = "", source = "The Guardian", url = "https://example.com"
        )
    }
    // Contenu "mock" : on réutilise tes cartes réutilisables
    MaterialTheme {
        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("SportApp", style = MaterialTheme.typography.headlineSmall)
            TipCard("Aperçu de la Home sans VM")
            Text("À la une • Football", style = MaterialTheme.typography.titleMedium)
            fake.take(2).forEach {
                FeaturedArticleCard(article = it, onClick = {})
            }
        }
    }
}
