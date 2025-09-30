package com.abone.abonex.domain.repository

import com.abone.abonex.domain.model.CardSpending
import com.abone.abonex.domain.model.DashboardSummary
import com.abone.abonex.util.Resource
import kotlinx.coroutines.flow.Flow

interface AnalyticsRepository {
    fun getDashboardSummary(): Flow<Resource<DashboardSummary>>
    fun getSpendingByCard(): Flow<Resource<List<CardSpending>>>

}