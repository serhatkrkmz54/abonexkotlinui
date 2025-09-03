package com.abone.abonex.ui.features

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abone.abonex.domain.model.PaymentHistory
import com.abone.abonex.domain.model.Subscription
import com.abone.abonex.domain.repository.SubscriptionRepository
import com.abone.abonex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SubscriptionDetailState(
    val isLoading: Boolean = true,
    val subscription: Subscription? = null,
    val paymentHistory: List<PaymentHistory> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class SubscriptionDetailViewModel @Inject constructor(
    private val repository: SubscriptionRepository,
    savedStateHandle: SavedStateHandle // Navigasyon argümanlarını almak için
) : ViewModel() {

    private val _state = MutableStateFlow(SubscriptionDetailState())
    val state = _state.asStateFlow()

    private val subscriptionId: Long = checkNotNull(savedStateHandle["subscriptionId"])

    init {
        loadDetails()
    }

    fun logPayment() {
        viewModelScope.launch {
            // Ödeme kaydetme başarılı olursa, verileri yenile
            val result = repository.logPayment(subscriptionId) // Bu metodu Repository'de oluşturmalısınız
            if (result is Resource.Success) {
                loadDetails() // Sayfayı en güncel verilerle yeniden yükle
            }
        }
    }

    private fun loadDetails() {
        viewModelScope.launch {
            repository.getSubscriptionDetails(subscriptionId).collect { result ->
                _state.update { currentState ->
                    when (result) {
                        is Resource.Loading -> currentState.copy(isLoading = true)
                        is Resource.Success -> currentState.copy(
                            isLoading = false,
                            subscription = result.data?.subscription,
                            paymentHistory = result.data?.paymentHistory ?: emptyList(),
                            error = null
                        )
                        is Resource.Error -> currentState.copy(
                            isLoading = false,
                            error = result.message
                        )
                        else -> currentState
                    }
                }
            }
        }
    }
}