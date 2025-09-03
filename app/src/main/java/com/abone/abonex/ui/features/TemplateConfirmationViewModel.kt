package com.abone.abonex.ui.features

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abone.abonex.data.remote.dto.subs.CreateSubscriptionFromPlanRequest
import com.abone.abonex.domain.repository.SubscriptionRepository
import com.abone.abonex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class TemplateConfirmState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val planId: Long? = null
)

@HiltViewModel
class TemplateConfirmViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TemplateConfirmState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.get<Long>("planId")?.let { planId ->
            _state.update { it.copy(planId = planId) }
        }
    }

    fun createSubscription(
        startDate: LocalDate,
        endDate: LocalDate?,
        cardName: String?,
        cardLastFour: String?,
        notificationDays: Int,
        firstPaymentMade: Boolean
    ) {
        val planId = _state.value.planId ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val request = CreateSubscriptionFromPlanRequest(
                planId = planId,
                startDate = startDate.toString(),
                endDate = endDate?.toString(),
                cardName = cardName?.takeIf { it.isNotBlank() },
                cardLastFourDigits = cardLastFour?.takeIf { it.isNotBlank() },
                notificationDaysBefore = notificationDays,
                firstPaymentMade = firstPaymentMade
            )

            val result = subscriptionRepository.createSubscriptionFromPlan(request)

            when (result) {
                is Resource.Success -> _state.update { it.copy(isLoading = false, isSuccess = true) }
                is Resource.Error -> _state.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }
}