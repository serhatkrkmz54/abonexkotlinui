package com.abone.abonex.domain.repository

import com.abone.abonex.data.remote.dto.subs.CreateSubscriptionRequest
import com.abone.abonex.domain.model.MonthlySpend
import com.abone.abonex.domain.model.Subscription
import com.abone.abonex.util.Resource
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    fun getUserSubscriptions(): Flow<Resource<List<Subscription>>>
    fun getCurrentMonthTotalSpend(): Flow<Resource<MonthlySpend>>
    suspend fun createManualSubscription(request: CreateSubscriptionRequest): Resource<Subscription>
    suspend fun refreshUserSubscriptions()
    suspend fun refreshMonthlySpend()

}