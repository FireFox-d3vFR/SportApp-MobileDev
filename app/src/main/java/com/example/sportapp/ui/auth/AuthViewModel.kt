package com.example.sportapp.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _isAuthenticated = MutableStateFlow(auth.currentUser != null)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    private val listener = FirebaseAuth.AuthStateListener { fa ->
        _isAuthenticated.value = (fa.currentUser != null)
    }

    init { auth.addAuthStateListener(listener) }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Login failed") }
    }

    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Register failed") }
    }

    fun logout() { auth.signOut() }

    override fun onCleared() {
        auth.removeAuthStateListener(listener)
        super.onCleared()
    }
}
