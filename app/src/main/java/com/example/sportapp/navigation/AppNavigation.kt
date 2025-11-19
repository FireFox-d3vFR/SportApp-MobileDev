package com.example.sportapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sportapp.R
import com.example.sportapp.ui.auth.RequireAuth
import com.example.sportapp.ui.components.AppTopBar
import com.example.sportapp.ui.screens.ArticlesScreen
import com.example.sportapp.ui.screens.HomeScreen
import com.example.sportapp.ui.screens.LoginScreen
import com.example.sportapp.ui.screens.RegisterScreen
import com.example.sportapp.ui.screens.SettingsScreen
import com.example.sportapp.ui.screens.FavoritesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentDestination = backStack?.destination
    val currentRoute = currentDestination?.route

    // bottom bar visible uniquement pour ces routes
    val bottomBarItems = listOf(
        NavigationItems.HOME,
        NavigationItems.ARTICLES,
        NavigationItems.FAVORITES
    )
    val hideBottomBar = currentRoute == NavigationItems.LOGIN.route ||
                        currentRoute == NavigationItems.REGISTER.route

    val currentItem = NavigationItems.entries.find { it.route == currentRoute }
    val pageTitle = currentItem?.let { stringResource(it.pageTitleRes) } ?: stringResource(R.string.app_name)

    val showBack = currentRoute in setOf(
        NavigationItems.SETTINGS.route,
        NavigationItems.LOGIN.route,
        NavigationItems.REGISTER.route
    )

    val showSettingsAction = currentRoute != NavigationItems.SETTINGS.route &&
            currentRoute != NavigationItems.LOGIN.route &&
            currentRoute != NavigationItems.REGISTER.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val isHome = currentRoute == NavigationItems.HOME.route
            if (!isHome) {
                AppTopBar(
                    title = pageTitle,
                    navigationIcon = if (showBack) {
                        {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
                            }
                        }
                    } else null,
                    actions = {
                        if (showSettingsAction) {
                            IconButton(onClick = {
                                navController.navigate(NavigationItems.SETTINGS.route) {
                                    launchSingleTop = true
                                }
                            }) {
                                Icon(
                                    imageVector = NavigationItems.SETTINGS.icon,
                                    contentDescription = stringResource(NavigationItems.SETTINGS.titleRes)
                                )
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            val isHome = currentRoute == NavigationItems.HOME.route
            if (!hideBottomBar && !isHome) {
                NavigationBar {
                    bottomBarItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = stringResource(item.titleRes)) },
                            label = { Text(stringResource(item.titleRes)) },
                            selected = selected,
                            onClick = {
                                if (item == NavigationItems.HOME) {
                                    navController.popBackStack(
                                        navController.graph.findStartDestination().id,
                                        inclusive = false
                                    )
                                } else if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationItems.HOME.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationItems.HOME.route) { HomeScreen(
                onOpenArticles = { navController.navigate(NavigationItems.ARTICLES.route) },
                onOpenFavorites = { navController.navigate(NavigationItems.FAVORITES.route) },
                onOpenSettings = { navController.navigate(NavigationItems.SETTINGS.route) { launchSingleTop = true } }
                )
            }

            composable(NavigationItems.ARTICLES.route) {
                RequireAuth(navController) { ArticlesScreen() }
            }

            composable(NavigationItems.FAVORITES.route) {
                RequireAuth(navController) { FavoritesScreen() } // stub pour l’instant
            }

            composable(NavigationItems.SETTINGS.route) {
                RequireAuth(navController) { SettingsScreen() } // stub pour l’instant
            }

            composable(NavigationItems.LOGIN.route) {
                LoginScreen(
                    onNavigateRegister = { navController.navigate(NavigationItems.REGISTER.route) },
                    onLoggedIn = {
                        navController.navigate(NavigationItems.HOME.route) {
                            popUpTo(NavigationItems.LOGIN.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(NavigationItems.REGISTER.route) {
                RegisterScreen(
                    onNavigateLogin = { navController.navigate(NavigationItems.LOGIN.route) },
                    onRegistered = {
                        navController.navigate(NavigationItems.HOME.route) {
                            popUpTo(NavigationItems.REGISTER.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
