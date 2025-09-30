package com.abone.abonex.data.remote.api.subs

import com.abone.abonex.data.remote.dto.subs.CardSpendingDto
import com.abone.abonex.data.remote.dto.subs.DashboardSummaryDto
import retrofit2.Response
import retrofit2.http.GET

interface AnalyticsApiService {
    @GET("api/v1/analytics/dashboard-summary")
    suspend fun getDashboardSummary(): Response<DashboardSummaryDto>

    @GET("api/v1/analytics/spending-by-card")
    suspend fun getSpendingByCard(): Response<List<CardSpendingDto>>
}