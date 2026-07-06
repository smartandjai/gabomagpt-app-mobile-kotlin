/* GabomaGPT · AuthViewModel.kt · SmartANDJ AI Technologies
   Auth state machine — drives navigation between Splash → Auth → Chat
   Fondateur : Daniel Jonathan ANDJ */

package com.smartandj.gabomagpt.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clerk.api.Clerk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Represents the three possible auth states for the app. */
sealed interface AuthState {
    /** SDK still loading / checking session */
    data object Loading : AuthState
    /** User is authenticated — contains display info */
    data class SignedIn(
        val userId: String,
        val fullName: String?,
        val avatarUrl: String?,
        val email: String?
    ) : AuthState
    /** No active session */
    data object SignedOut : AuthState
}

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        observeClerkSession()
    }

    private fun observeClerkSession() {
        viewModelScope.launch {
            // Wait for Clerk SDK to be fully initialized
            while (!Clerk.isInitialized) {
                kotlinx.coroutines.delay(100)
            }

            // Observe the current user — Clerk updates this reactively
            // on sign-in, sign-out, and token refresh
            Clerk.userFlow.collect { user ->
                _authState.value = if (user != null) {
                    AuthState.SignedIn(
                        userId = user.id,
                        fullName = "${user.firstName ?: ""} ${user.lastName ?: ""}".trim()
                            .ifEmpty { null },
                        avatarUrl = user.imageUrl,
                        email = user.primaryEmailAddress?.emailAddress
                    )
                } else {
                    AuthState.SignedOut
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            Clerk.signOut()
        }
    }
}
