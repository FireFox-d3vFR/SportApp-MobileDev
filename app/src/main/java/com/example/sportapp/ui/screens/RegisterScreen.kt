package com.example.sportapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportapp.ui.auth.AuthViewModel

@Composable
fun RegisterScreen(
    onNavigateLogin: () -> Unit,
    onRegistered: () -> Unit
) {
    val vm: AuthViewModel = viewModel()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Inscription", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Mot de passe (≥ 6)") }, singleLine = true
        )

        Button(
            enabled = !isLoading && email.isNotBlank() && password.length >= 6,
            onClick = {
                isLoading = true
                vm.register(
                    email.trim(), password,
                    onSuccess = { isLoading = false; onRegistered() },
                    onError   = { isLoading = false; error = it }
                )
            }
        ) { Text(if (isLoading) "Loading..." else "Register") }

        if (error.isNotBlank()) Text(error, color = MaterialTheme.colorScheme.error)

        TextButton(onClick = onNavigateLogin) { Text("J’ai déjà un compte") }
    }
}

@Composable
private fun RegisterScreenContent(
    email: String,
    password: String,
    isLoading: Boolean,
    error: String?,
    onEmail: (String) -> Unit,
    onPassword: (String) -> Unit,
    onSubmit: () -> Unit,
    onNavigateLogin: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Inscription", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = email, onValueChange = onEmail, label = { Text("Email") }, singleLine = true)
        OutlinedTextField(value = password, onValueChange = onPassword, label = { Text("Mot de passe (≥ 6)") }, singleLine = true)
        Button(enabled = !isLoading && email.isNotBlank() && password.length >= 6, onClick = onSubmit) {
            Text(if (isLoading) "Loading..." else "Register")
        }
        if (!error.isNullOrBlank()) Text(error, color = MaterialTheme.colorScheme.error)
        TextButton(onClick = onNavigateLogin) { Text("J’ai déjà un compte") }
    }
}

@Preview(name = "Register - Light", showBackground = true)
@Composable
private fun Preview_Register() {
    RegisterScreenContent(
        email = "demo@mail.com",
        password = "",
        isLoading = false,
        error = null,
        onEmail = {}, onPassword = {}, onSubmit = {}, onNavigateLogin = {}
    )
}