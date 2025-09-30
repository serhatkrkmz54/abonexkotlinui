package com.abone.abonex.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abone.abonex.domain.model.CardSpending
import com.abone.abonex.domain.model.DashboardSummary
import com.abone.abonex.domain.repository.AnalyticsRepository
import com.abone.abonex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalyticsState(
    val isLoading: Boolean = true,
    val summary: DashboardSummary? = null,
    val spendingByCard: List<CardSpending> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AnalyticsState())
    val state = _state.asStateFlow()
    val spendingByCard: List<CardSpending> = emptyList()

    init {
        loadAnalyticsData()
    }

    fun loadAnalyticsData() {
        viewModelScope.launch {
            analyticsRepository.getDashboardSummary().collect { result ->
                _state.update {
                    when(result) {
                        is Resource.Loading -> it.copy(isLoading = true)
                        is Resource.Success -> it.copy(isLoading = false, summary = result.data)
                        is Resource.Error -> it.copy(isLoading = false, error = result.message)
                        else -> it
                    }
                }
            }
        }
        viewModelScope.launch {
            analyticsRepository.getSpendingByCard().collect { result ->
                if (result is Resource.Success) {
                    _state.update { it.copy(spendingByCard = result.data ?: emptyList()) }
                }
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = false) }
        }
    }
}
