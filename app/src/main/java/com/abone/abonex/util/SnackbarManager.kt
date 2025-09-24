package com.abone.abonex.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnackbarManager @Inject constructor() {
    private val _messages: MutableStateFlow<Pair<String, Long>?> = MutableStateFlow(null)
    val messages: StateFlow<Pair<String, Long>?> = _messages.asStateFlow()

    fun showMessage(message: String) {
        _messages.value = Pair(message, System.currentTimeMillis())
    }

    fun clearMessage() {
        _messages.value = null
    }
}