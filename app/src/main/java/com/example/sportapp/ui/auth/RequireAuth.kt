package com.example.sportapp.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun RequireAuth(
    navController: NavController,
    content: @Composable () -> Unit
) {
    val vm: AuthViewModel = viewModel()
    val authed by vm.isAuthenticated.collectAsState()

    LaunchedEffect(authed) {
        if (!authed) {
            val current = navController.currentBackStackEntry?.destination?.route
            if (current != "login" && current != "register") {
                navController.navigate("login") {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = false
                        inclusive = false
                    }
                    launchSingleTop = true
                    restoreState = false
                }
            }
        }
    }

    if (authed) content()
}
