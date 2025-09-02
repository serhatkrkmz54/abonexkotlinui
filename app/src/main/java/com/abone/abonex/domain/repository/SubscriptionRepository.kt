package com.abone.abonex.domain.repository

import com.abone.abonex.domain.model.MonthlySpend
import com.abone.abonex.domain.model.Subscription
import com.abone.abonex.util.Resource
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    fun getUserSubscriptions(): Flow<Resource<List<Subscription>>>
    fun getCurrentMonthTotalSpend(): Flow<Resource<MonthlySpend>>
}