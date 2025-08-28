package com.abone.abonex.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abone.abonex.data.local.TokenManager
import com.abone.abonex.data.remote.dto.LoginRequest
import com.abone.abonex.domain.repository.AuthRepository
import com.abone.abonex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<String>>(Resource.Idle())
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading()

            val request = LoginRequest(email = email, password = password)
            val result = authRepository.login(request)

            when (result) {
                is Resource.Success -> {
                    val token = result.data?.token
                    if (token != null) {
                        tokenManager.saveToken(token)
                        _loginState.value = Resource.Success("Giriş başarılı!")
                    } else {
                        _loginState.value = Resource.Error("Token alınamadı.")
                    }
                }
                is Resource.Error -> {
                    _loginState.value = Resource.Error(result.message ?: "Bilinmeyen bir hata oluştu.")
                }
                else -> {}
            }
        }
    }
}