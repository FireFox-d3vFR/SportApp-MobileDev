package com.example.sportapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import androidx.compose.ui.platform.LocalContext
import com.example.sportapp.data.model.ArticleDto

@Composable
fun ArticleCard(
    article: ArticleDto,
    modifier: Modifier = Modifier,
    saved: Boolean,
    onToggleSave: (ArticleDto) -> Unit,
    onClick: (() -> Unit)? = null
) {
    Card(
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = article.category,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { onToggleSave(article) }) {
                    Icon(
                        imageVector = if (saved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = if (saved) "Remove from favorites" else "Save to favorites"
                    )
                }
            }

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
        }
    }
}
