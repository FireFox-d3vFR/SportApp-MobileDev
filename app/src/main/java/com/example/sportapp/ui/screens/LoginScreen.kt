package com.example.sportapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportapp.ui.auth.AuthViewModel

@Composable
fun LoginScreen(
    onNavigateRegister: () -> Unit,
    onLoggedIn: () -> Unit
) {
    val vm: AuthViewModel = viewModel()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var pwVisible by rememberSaveable { mutableStateOf(false) }

    fun submit() {
        if (email.isBlank() || password.isBlank() || isLoading) return
        isLoading = true
        vm.login(
            email.trim(), password,
            onSuccess = { isLoading = false; onLoggedIn() },
            onError   = { isLoading = false; error = it }
        )
    }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Connexion", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Mot de passe") }, singleLine = true,
            visualTransformation = if (pwVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { pwVisible = !pwVisible }) {
                    Icon(if (pwVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { submit() })
        )

        Button(
            enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
            onClick = { submit() }
        ) { Text(if (isLoading) "Loading..." else "Login") }

        if (error.isNotBlank()) Text(error, color = MaterialTheme.colorScheme.error)

        TextButton(onClick = onNavigateRegister) { Text("Créer un compte") }
    }
}

@Composable
private fun LoginScreenContent(
    email: String,
    password: String,
    isLoading: Boolean,
    error: String?,
    onEmail: (String) -> Unit,
    onPassword: (String) -> Unit,
    onSubmit: () -> Unit,
    onNavigateRegister: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Connexion", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = email, onValueChange = onEmail, label = { Text("Email") }, singleLine = true)
        OutlinedTextField(value = password, onValueChange = onPassword, label = { Text("Mot de passe") }, singleLine = true)
        Button(enabled = !isLoading && email.isNotBlank() && password.isNotBlank(), onClick = onSubmit) {
            Text(if (isLoading) "Loading..." else "Login")
        }
        if (!error.isNullOrBlank()) Text(error, color = MaterialTheme.colorScheme.error)
        TextButton(onClick = onNavigateRegister) { Text("Créer un compte") }
    }
}

@Preview(name = "Login - Light", showBackground = true)
@Composable
private fun Preview_Login() {
    LoginScreenContent(
        email = "demo@mail.com",
        password = "•••••••",
        isLoading = false,
        error = null,
        onEmail = {}, onPassword = {}, onSubmit = {}, onNavigateRegister = {}
    )
}
