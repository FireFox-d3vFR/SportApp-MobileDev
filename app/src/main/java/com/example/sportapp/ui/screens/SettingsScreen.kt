package com.example.sportapp.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.sportapp.R
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportapp.data.settings.ThemeMode
import com.example.sportapp.ui.auth.AuthViewModel
import com.example.sportapp.ui.settings.SECTION_OPTIONS
import com.example.sportapp.ui.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val vm: SettingsViewModel = viewModel()
    val state by vm.state.collectAsState()
    val authVm: AuthViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ---- Thème ----
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Apparence", style = MaterialTheme.typography.titleMedium)

                ThemeRow(
                    selected = state.theme,
                    onSelect = { vm.setTheme(it) }
                )
            }
        }

        // ---- Langue ----
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.settings_language), style = MaterialTheme.typography.titleMedium)

                var expanded by remember { mutableStateOf(false) }
                // Liste des tages -> labels
                val languages = listOf( "fr" to stringResource(R.string.lang_fr),
                                        "en" to stringResource(R.string.lang_en))
                val current = languages.firstOrNull { it.first == state.language }?.second ?: "Français"

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded}
                ) {
                    OutlinedTextField(
                        value = current,
                        onValueChange = {},
                        label = { Text(stringResource(R.string.settings_language)) },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        languages.forEach { (tag, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    expanded = false
                                    vm.setLanguage(tag)
                                }
                            )
                        }
                    }
                }
                Text(
                    text = "Change la langue immédiatement dans l'app.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ---- Section par défaut ----
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Section par défaut", style = MaterialTheme.typography.titleMedium)

                var expanded by remember { mutableStateOf(false) }
                val currentLabel = SECTION_OPTIONS.firstOrNull { it.first == state.defaultSectionId }?.second
                    ?: "Football"

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = currentLabel,
                        onValueChange = {},
                        label = { Text("Section") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        SECTION_OPTIONS.forEach { (id, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    expanded = false
                                    vm.setDefaultSection(id)
                                }
                            )
                        }
                    }
                }
                Text(
                    "Cette section sera utilisée par défaut pour le flux d’articles.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ---- Déconnexion ----
        Button(onClick = { authVm.logout() }) {
            Text("Se déconnecter")
        }
    }
}

@Composable
private fun ThemeRow(
    selected: ThemeMode,
    onSelect: (ThemeMode) -> Unit
) {
    @Composable
    fun Item(mode: ThemeMode, label: String) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButton(
                selected = selected == mode,
                onClick = { onSelect(mode) }
            )
            Text(label, modifier = Modifier.padding(top = 12.dp))
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Item(ThemeMode.SYSTEM, "Système")
        Item(ThemeMode.LIGHT, "Clair")
        Item(ThemeMode.DARK, "Sombre")
    }
}

@Composable
private fun SettingsContentPreview(
    theme: ThemeMode,
    language: String,
    defaultSectionId: String
) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Apparence", style = MaterialTheme.typography.titleMedium)
        Text("• Thème: $theme")
        Text("• Langue: $language")
        Text("• Section par défaut: $defaultSectionId")
    }
}

@Preview(name= "Settings - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Settings - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview_Settings() {
    SettingsContentPreview(
        theme = ThemeMode.LIGHT,
        language = "fr",
        defaultSectionId = "football"
    )
}
