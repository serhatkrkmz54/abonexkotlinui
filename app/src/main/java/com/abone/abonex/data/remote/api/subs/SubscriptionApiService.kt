package com.abone.abonex.data.remote.api.subs

import com.abone.abonex.data.remote.dto.subs.CreateSubscriptionFromPlanRequest
import com.abone.abonex.data.remote.dto.subs.CreateSubscriptionRequest
import com.abone.abonex.data.remote.dto.subs.HomeSubscriptionsDto
import com.abone.abonex.data.remote.dto.subs.MonthlySpendDto
import com.abone.abonex.data.remote.dto.subs.PaymentHistoryDto
import com.abone.abonex.data.remote.dto.subs.SubscriptionDetailDto
import com.abone.abonex.data.remote.dto.subs.SubscriptionDto
import com.abone.abonex.data.remote.dto.subs.TemplateWithPlansDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SubscriptionApiService{
    @GET("/api/v1/subscriptions/get-subs")
    suspend fun getUserSubscriptions(): Response<List<SubscriptionDto>>

    @GET("api/v1/subscriptions/monthly-cost")
    suspend fun getCurrentMonthTotalSpend(): Response<MonthlySpendDto>

    @POST("api/v1/subscriptions/manual")
    suspend fun createManualSubscription(
        @Body request: CreateSubscriptionRequest
    ): Response<SubscriptionDto>

    @GET("api/v1/subscriptions/categorized-home-view")
    suspend fun getHomeViewSubscriptions(): Response<HomeSubscriptionsDto>

    @GET("api/v1/subscription-templates/get-all-templates")
    suspend fun getAllTemplates(): Response<List<TemplateWithPlansDto>>

    @POST("api/v1/subscriptions/from-plan")
    suspend fun createSubscriptionFromPlan(
        @Body request: CreateSubscriptionFromPlanRequest
    ): Response<SubscriptionDto>

    @GET("api/v1/subscriptions/subscription-detail/{id}")
    suspend fun getSubscriptionDetails(@Path("id") id: Long): Response<SubscriptionDetailDto>

    @POST("api/v1/subscriptions/{id}/log-payment")
    suspend fun logPayment(@Path("id") id: Long): Response<PaymentHistoryDto>

}