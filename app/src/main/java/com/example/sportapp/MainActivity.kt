package com.example.sportapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportapp.navigation.AppNavigation
import com.example.sportapp.ui.settings.SettingsViewModel
import com.example.sportapp.ui.theme.SportAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settingsVm: SettingsViewModel = viewModel()
            val settings by settingsVm.state.collectAsState()

            SportAppTheme(themeMode = settings.theme) {
                // Pas de Scaffold ici : AppNavigation contient déjà un Scaffold
                AppNavigation()
            }
        }
    }
}
