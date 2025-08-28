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

    // UI'ın dinleyeceği state. Başlangıçta null.
    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        // ViewModel oluşturulur oluşturulmaz token kontrolünü başlat.
        checkToken()
    }

    private fun checkToken() {
        viewModelScope.launch {
            // Splash ekranının en az 1.5 saniye görünmesini sağlayalım (UX için)
            delay(1500L)

            // DataStore'dan token'ı bir kerelik oku.
            val token = tokenManager.getToken().first()

            if (token.isNullOrBlank()) {
                // Token yoksa, Login ekranına gitmesi için yönlendirme yapma,
                // kullanıcı butona basarak ilerlesin.
                // Ancak bazı senaryolarda doğrudan Login'e de yollayabiliriz.
                // Şimdilik null bırakarak kullanıcının butonunu bekliyoruz.
                _startDestination.value = AppRoute.LOGIN_SCREEN
            } else {
                // Token varsa, direkt Home ekranına git.
                _startDestination.value = AppRoute.HOME_SCREEN
            }
        }
    }
}