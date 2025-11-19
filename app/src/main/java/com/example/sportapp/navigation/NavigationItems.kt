package com.example.sportapp.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.sportapp.R

enum class NavigationItems(
    val route: String,
    @param:StringRes val titleRes: Int,
    @param:StringRes val pageTitleRes: Int,
    val icon: ImageVector
) {
    HOME("home", R.string.nav_home, R.string.title_home, Icons.Filled.Home),
    ARTICLES("articles", R.string.nav_articles, R.string.title_articles, Icons.Filled.Article),
    FAVORITES("favorites", R.string.nav_favorites, R.string.title_favorites, Icons.Filled.Bookmark),

    SETTINGS("settings", R.string.nav_settings, R.string.title_settings, Icons.Filled.Settings),

    LOGIN("login", R.string.nav_login, R.string.title_login, Icons.AutoMirrored.Filled.Login),
    REGISTER("register", R.string.nav_register, R.string.title_register, Icons.Filled.PersonAdd)
}