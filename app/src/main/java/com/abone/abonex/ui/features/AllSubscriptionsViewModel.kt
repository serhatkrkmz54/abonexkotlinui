package com.abone.abonex.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abone.abonex.domain.model.Template
import com.abone.abonex.domain.repository.SubscriptionRepository
import com.abone.abonex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AllSubscriptionsState(
    val isLoading: Boolean = true,
    val allTemplates: List<Template> = emptyList(),
    val displayedTemplates: List<Template> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null
)

@HiltViewModel
class AllSubscriptionsViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AllSubscriptionsState())
    val state = _state.asStateFlow()

    init {
        loadTemplates()
    }

    private fun loadTemplates() {
        viewModelScope.launch {
            subscriptionRepository.getTemplates().collect { result ->
                _state.update { currentState ->
                    when (result) {
                        is Resource.Loading -> currentState.copy(isLoading = true)
                        is Resource.Success -> {
                            val templates = result.data ?: emptyList()
                            currentState.copy(
                                isLoading = false,
                                allTemplates = templates,
                                displayedTemplates = templates,
                                error = null
                            )
                        }
                        is Resource.Error -> currentState.copy(
                            isLoading = false,
                            error = result.message ?: "Şablonlar yüklenemedi."
                        )
                        else -> currentState
                    }
                }
            }
        }
    }

    /**
     * Arama çubuğundaki metin değiştiğinde çağrılır.
     * @param query Yeni arama metni.
     */
    fun onSearchQueryChanged(query: String) {
        _state.update { currentState ->
            val filteredList = if (query.isBlank()) {
                currentState.allTemplates
            } else {
                currentState.allTemplates.filter {
                    it.name.contains(query, ignoreCase = true)
                }
            }
            currentState.copy(
                searchQuery = query,
                displayedTemplates = filteredList
            )
        }
    }
}