package com.abone.abonex.domain.repository

import com.abone.abonex.data.remote.dto.AuthResponse
import com.abone.abonex.data.remote.dto.LoginRequest
import com.abone.abonex.data.remote.dto.ReactivateRequest
import com.abone.abonex.data.remote.dto.RegisterRequest
import com.abone.abonex.data.remote.dto.UserDto
import com.abone.abonex.data.remote.dto.VerifyCodeRequest
import com.abone.abonex.util.Resource

interface AuthRepository {
    suspend fun login(request: LoginRequest): Resource<AuthResponse>
    suspend fun register(request: RegisterRequest): Resource<AuthResponse>
    suspend fun requestReactivationOtp(request: ReactivateRequest): Resource<Unit>
    suspend fun verifyReactivationOtp(request: VerifyCodeRequest): Resource<AuthResponse>

}