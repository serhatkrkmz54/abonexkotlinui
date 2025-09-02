package com.abone.abonex.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abone.abonex.data.remote.dto.subs.CreateSubscriptionRequest
import com.abone.abonex.domain.use_case.CreateManualSubscriptionUseCase
import com.abone.abonex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddSubscriptionState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AddSubscriptionViewModel @Inject constructor(
    private val createManualSubscriptionUseCase: CreateManualSubscriptionUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AddSubscriptionState())
    val state = _state.asStateFlow()

    fun createSubscription(request: CreateSubscriptionRequest) {
        viewModelScope.launch {
            createManualSubscriptionUseCase(request).collect { result ->
                when(result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _state.update { it.copy(isLoading = false, success = true) }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.message) }
                    }
                    else -> {}
                }
            }
        }
    }
}