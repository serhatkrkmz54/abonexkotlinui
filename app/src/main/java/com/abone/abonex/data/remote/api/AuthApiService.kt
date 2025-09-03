package com.abone.abonex.data.remote.api

import com.abone.abonex.data.remote.dto.AuthResponse
import com.abone.abonex.data.remote.dto.LoginRequest
import com.abone.abonex.data.remote.dto.ReactivateRequest
import com.abone.abonex.data.remote.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService{
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/reactivate-account")
    suspend fun reactiveAccount(@Body request: ReactivateRequest): Response<AuthResponse>

}