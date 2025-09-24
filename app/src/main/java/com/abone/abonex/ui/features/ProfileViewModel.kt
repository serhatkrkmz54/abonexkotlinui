package com.abone.abonex.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abone.abonex.data.local.TokenManager
import com.abone.abonex.data.remote.dto.UserDto
import com.abone.abonex.data.remote.dto.UserProfileUpdateRequest
import com.abone.abonex.domain.repository.AuthRepository
import com.abone.abonex.domain.repository.UserRepository
import com.abone.abonex.util.Resource
import com.abone.abonex.util.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userRepository.loadUserProfile()
            userRepository.getCachedUserProfile()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { user ->
                    _uiState.update { it.copy(isLoading = false, user = user, error = null) }
                }
        }
    }

    fun updateUserProfile(request: UserProfileUpdateRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, updateError = null) }
            when (val result = userRepository.updateUserProfile(request)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    snackbarManager.showMessage("Profil başarıyla güncellendi!")
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, updateError = result.message) }
                    snackbarManager.showMessage("Hata: ${result.message}")
                }
                else -> {}
            }
        }
    }

    fun clearUpdateError() {
        _uiState.update { it.copy(updateError = null) }
    }

    fun deactivateAccount() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = userRepository.deactivateAccount()
            if (result is Resource.Success) {
                tokenManager.clearToken()
                _uiState.update { it.copy(isLoading = false, isDeactivated = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        updateError = result.message ?: "Hesap pasif hale getirilemedi."
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearToken()
            _uiState.update { it.copy(isLoggedOut = true) }
        }
    }
}