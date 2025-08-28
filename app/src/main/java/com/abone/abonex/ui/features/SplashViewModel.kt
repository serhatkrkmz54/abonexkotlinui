package com.abone.abonex.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abone.abonex.data.local.TokenManager
import com.abone.abonex.navigation.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        checkToken()
    }

    private fun checkToken() {
        viewModelScope.launch {
            delay(1500L)

            val token = tokenManager.getToken().first()

            if (token.isNullOrBlank()) {
                _startDestination.value = AppRoute.WELCOME_SCREEN
            } else {
                _startDestination.value = AppRoute.HOME_SCREEN
            }
        }
    }
}