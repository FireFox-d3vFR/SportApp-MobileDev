package com.example.sportapp.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState

suspend fun SnackbarHostState.quick(
    message: String,
    action: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short
) = showSnackbar(message, actionLabel = action, withDismissAction = true, duration = duration)