package com.abone.abonex.data.remote.api.subs

import com.abone.abonex.data.remote.dto.subs.MonthlySpendDto
import com.abone.abonex.data.remote.dto.subs.SubscriptionDto
import retrofit2.Response
import retrofit2.http.GET

interface SubscriptionApiService{
    @GET("/api/v1/subscriptions/get-subs")
    suspend fun getUserSubscriptions(): Response<List<SubscriptionDto>>

    @GET("api/v1/subscriptions/monthly-cost")
    suspend fun getCurrentMonthTotalSpend(): Response<MonthlySpendDto>
}