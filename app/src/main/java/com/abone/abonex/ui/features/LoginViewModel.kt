package com.abone.abonex.ui.features

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abone.abonex.data.local.TokenManager
import com.abone.abonex.data.remote.dto.LoginRequest
import com.abone.abonex.data.remote.dto.ReactivateRequest
import com.abone.abonex.domain.repository.AuthRepository
import com.abone.abonex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginResult {
    object Idle : LoginResult()
    object Loading : LoginResult()
    data class Success(val message: String) : LoginResult()
    data class Error(val message: String, val isAccountInactive: Boolean = false) : LoginResult()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginState = _loginState.asStateFlow()

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    fun login() {
        viewModelScope.launch {
            _loginState.value = LoginResult.Loading

            val request = LoginRequest(email = email, password = password)
            val result = authRepository.login(request)

            when (result) {
                is Resource.Success -> {
                    val token = result.data?.token
                    if (token != null) {
                        tokenManager.saveToken(token)
                        _loginState.value = LoginResult.Success("Giriş başarılı!")
                    } else {
                        _loginState.value = LoginResult.Error("Token alınamadı.")
                    }
                }
                is Resource.Error -> {
                    val isInactive = result.message?.contains("HESAP_PASIF") == true
                    _loginState.value = LoginResult.Error(
                        message = if (isInactive) "Hesabınız pasif durumda." else result.message ?: "Bilinmeyen bir hata oluştu.",
                        isAccountInactive = isInactive
                    )
                }
                else -> {}
            }
        }
    }
    fun reactivateAccount() {
        viewModelScope.launch {
            _loginState.value = LoginResult.Loading

            val request = ReactivateRequest(email = email, password = password)
            val result = authRepository.reactivateAccount(request)

            when (result) {
                is Resource.Success -> {
                    val token = result.data?.token
                    if (token != null) {
                        tokenManager.saveToken(token)
                        _loginState.value = LoginResult.Success("Hesap başarıyla aktifleştirildi!")
                    } else {
                        _loginState.value = LoginResult.Error("Token alınamadı.")
                    }
                }
                is Resource.Error -> {
                    _loginState.value = LoginResult.Error(result.message ?: "Bilinmeyen bir hata oluştu.")
                }
                else -> {}
            }
        }
    }
}