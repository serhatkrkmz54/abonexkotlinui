package com.abone.abonex.ui.features

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abone.abonex.data.local.TokenManager
import com.abone.abonex.data.remote.dto.UserDto
import com.abone.abonex.domain.model.MonthlySpend
import com.abone.abonex.domain.model.Subscription
import com.abone.abonex.domain.repository.SubscriptionRepository
import com.abone.abonex.domain.repository.UserRepository
import com.abone.abonex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

data class HomeUiState(
    val isLoading: Boolean = true,
    val user: UserDto? = null,
    val subscriptions: List<Subscription> = emptyList(),
    val monthlySpend: MonthlySpend? = null,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager,
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {

        observeUserProfile()
        observeSubscriptions()
        observeMonthlySpend()

        refreshAllData()
    }

    fun refreshAllData() {
        viewModelScope.launch {
            userRepository.loadUserProfile()
            subscriptionRepository.refreshUserSubscriptions()
            subscriptionRepository.refreshMonthlySpend()
        }
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            userRepository.getCachedUserProfile()
                .catch { e -> _uiState.update { it.copy(error = e.message) } }
                .collect { user -> _uiState.update { it.copy(user = user) } }
        }
    }

    private fun observeSubscriptions() {
        viewModelScope.launch {
            subscriptionRepository.getUserSubscriptions().collect { result ->
                _uiState.update { currentState ->
                    when (result) {
                        is Resource.Loading -> currentState.copy(isLoading = true)
                        is Resource.Success -> currentState.copy(
                            isLoading = false,
                            subscriptions = result.data ?: emptyList(),
                            error = null
                        )
                        is Resource.Error -> currentState.copy(
                            isLoading = false,
                            error = result.message ?: "Abonelikler yüklenemedi."
                        )
                        else -> currentState
                    }
                }
            }
        }
    }

    private fun observeMonthlySpend() {
        viewModelScope.launch {
            subscriptionRepository.getCurrentMonthTotalSpend().collect { result ->
                if (result is Resource.Success) {
                    _uiState.update { it.copy(monthlySpend = result.data) }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearToken()
        }
    }
}