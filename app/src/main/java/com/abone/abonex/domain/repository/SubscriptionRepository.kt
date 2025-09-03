package com.abone.abonex.domain.repository

import com.abone.abonex.data.remote.dto.subs.CreateSubscriptionFromPlanRequest
import com.abone.abonex.data.remote.dto.subs.CreateSubscriptionRequest
import com.abone.abonex.domain.model.HomeSubscriptions
import com.abone.abonex.domain.model.MonthlySpend
import com.abone.abonex.domain.model.Subscription
import com.abone.abonex.domain.model.SubscriptionDetail
import com.abone.abonex.domain.model.Template
import com.abone.abonex.util.Resource
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    fun getHomeViewSubscriptions(): Flow<Resource<HomeSubscriptions>>
    fun getCurrentMonthTotalSpend(): Flow<Resource<MonthlySpend>>
    fun getTemplates(): Flow<Resource<List<Template>>>
    fun getSubscriptionDetails(id: Long): Flow<Resource<SubscriptionDetail>>
    suspend fun createManualSubscription(request: CreateSubscriptionRequest): Resource<Subscription>
    suspend fun refreshHomeViewSubscriptions()
    suspend fun refreshMonthlySpend()
    suspend fun createSubscriptionFromPlan(request: CreateSubscriptionFromPlanRequest): Resource<Subscription>
    suspend fun logPayment(id: Long): Resource<Unit>
    suspend fun cancelSubscription(id: Long): Resource<Unit>

}