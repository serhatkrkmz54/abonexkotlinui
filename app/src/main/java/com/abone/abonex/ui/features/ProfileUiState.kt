package com.abone.abonex.ui.features

import com.abone.abonex.data.remote.dto.UserDto

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: UserDto? = null,
    val error: String? = null,
    val updateError: String? = null,
    val isDeactivated: Boolean = false,
    val isLoggedOut: Boolean = false
)