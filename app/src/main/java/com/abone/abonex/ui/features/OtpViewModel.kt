package com.abone.abonex.ui.features

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abone.abonex.data.local.TokenManager
import com.abone.abonex.data.remote.dto.ReactivateRequest
import com.abone.abonex.data.remote.dto.VerifyCodeRequest
import com.abone.abonex.domain.repository.AuthRepository
import com.abone.abonex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OtpState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val canResend: Boolean = false,
    val resendTimer: Int = 60
)

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    val email: String = checkNotNull(savedStateHandle["email"])
    private val password: String = checkNotNull(savedStateHandle["password"])

    init {
        startResendTimer()
    }

    fun verifyOtp(code: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val request = VerifyCodeRequest(email, code)
            when (val result = authRepository.verifyReactivationOtp(request)) {
                is Resource.Success -> {
                    result.data?.token?.let { token ->
                        tokenManager.saveToken(token)
                        _state.update { it.copy(isLoading = false, isSuccess = true) }
                    } ?: _state.update { it.copy(isLoading = false, error = "Token alınamadı.") }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
                else -> {}
            }
        }
    }

    fun resendOtp() {
        if (!_state.value.canResend) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val request = ReactivateRequest(email, password)
            when (authRepository.requestReactivationOtp(request)) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    startResendTimer()
                }
                is Resource.Error -> _state.update { it.copy(isLoading = false, error = "Kod gönderilemedi.") }
                else -> {}
            }
        }
    }

    private fun startResendTimer() {
        viewModelScope.launch {
            _state.update { it.copy(canResend = false, resendTimer = 60) }
            for (i in 60 downTo 1) {
                _state.update { it.copy(resendTimer = i) }
                delay(1000L)
            }
            _state.update { it.copy(canResend = true) }
        }
    }
}