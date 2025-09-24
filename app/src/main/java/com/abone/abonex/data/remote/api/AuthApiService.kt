package com.abone.abonex.data.remote.api

import com.abone.abonex.data.remote.dto.AuthResponse
import com.abone.abonex.data.remote.dto.LoginRequest
import com.abone.abonex.data.remote.dto.ReactivateRequest
import com.abone.abonex.data.remote.dto.RegisterRequest
import com.abone.abonex.data.remote.dto.VerifyCodeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService{
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/request-reactivation-otp")
    suspend fun requestReactivationOtp(@Body request: ReactivateRequest): Response<Unit>

    @POST("api/auth/verify-reactivation-otp")
    suspend fun verifyReactivationOtp(@Body request: VerifyCodeRequest): Response<AuthResponse>

}