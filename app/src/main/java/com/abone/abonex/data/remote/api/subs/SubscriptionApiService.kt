package com.abone.abonex.data.remote.api.subs

import com.abone.abonex.data.remote.dto.subs.CreateSubscriptionRequest
import com.abone.abonex.data.remote.dto.subs.MonthlySpendDto
import com.abone.abonex.data.remote.dto.subs.SubscriptionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SubscriptionApiService{
    @GET("/api/v1/subscriptions/get-subs")
    suspend fun getUserSubscriptions(): Response<List<SubscriptionDto>>

    @GET("api/v1/subscriptions/monthly-cost")
    suspend fun getCurrentMonthTotalSpend(): Response<MonthlySpendDto>

    @POST("api/v1/subscriptions/manual")
    suspend fun createManualSubscription(
        @Body request: CreateSubscriptionRequest
    ): Response<SubscriptionDto>
}