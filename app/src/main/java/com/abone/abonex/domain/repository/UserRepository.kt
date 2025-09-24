package com.abone.abonex.domain.repository

import com.abone.abonex.data.remote.dto.UserDto
import com.abone.abonex.data.remote.dto.UserProfileUpdateRequest
import com.abone.abonex.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCachedUserProfile(): Flow<UserDto>
    suspend fun loadUserProfile(): Resource<Unit>
    suspend fun updateUserProfile(request: UserProfileUpdateRequest): Resource<UserDto>
    suspend fun deactivateAccount(): Resource<Unit>
    suspend fun updateFcmToken(token: String): Resource<Unit>
}
