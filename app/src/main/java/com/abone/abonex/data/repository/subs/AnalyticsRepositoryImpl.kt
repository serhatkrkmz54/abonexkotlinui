package com.abone.abonex.data.repository.subs

import com.abone.abonex.data.mapper.toDomain
import com.abone.abonex.data.remote.api.subs.AnalyticsApiService
import com.abone.abonex.domain.model.CardSpending
import com.abone.abonex.domain.model.DashboardSummary
import com.abone.abonex.domain.repository.AnalyticsRepository
import com.abone.abonex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(
    private val api: AnalyticsApiService
) : AnalyticsRepository {
    override fun getDashboardSummary(): Flow<Resource<DashboardSummary>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getDashboardSummary()
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!.toDomain()))
            } else {
                emit(Resource.Error("Veriler alınamadı."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message))
        }
    }

    override fun getSpendingByCard(): Flow<Resource<List<CardSpending>>> = flow {
        try {
            val response = api.getSpendingByCard()
            if (response.isSuccessful && response.body() != null) {
                val spendingList = response.body()!!.map { it.toDomain() }
                emit(Resource.Success(spendingList))
            } else {
                emit(Resource.Error("Kart harcamaları alınamadı: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Bilinmeyen bir ağ hatası oluştu."))
        }
    }
}