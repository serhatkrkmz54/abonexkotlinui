package com.abone.abonex.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abone.abonex.domain.model.Notification
import com.abone.abonex.domain.repository.NotificationRepository
import com.abone.abonex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationsState())
    val state = _state.asStateFlow()

    init {
        observeNotifications()
        viewModelScope.launch {
            repository.refreshNotifications()
        }
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            repository.getNotifications().collect { result ->
                when (result) {
                    is Resource.Loading -> _state.update { it.copy(isLoading = true) }
                    is Resource.Success -> {

                        _state.update { it.copy(isLoading = false, notifications = result.data ?: emptyList()) }
                    }
                    is Resource.Error -> _state.update { it.copy(isLoading = false, error = result.message) }
                    else->{}
                }
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch { repository.markAllAsRead() }
    }

    fun refreshNotifications() {
        viewModelScope.launch { repository.refreshNotifications() }
    }


    fun markAsRead(id: Long) {
        viewModelScope.launch { repository.markAsRead(id) }
    }
}