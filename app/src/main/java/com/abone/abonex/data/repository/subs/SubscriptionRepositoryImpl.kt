package com.abone.abonex.data.repository.subs

import android.util.Log
import com.abone.abonex.data.mapper.toMonthlySpend
import com.abone.abonex.data.mapper.toSubscription
import com.abone.abonex.data.remote.api.subs.SubscriptionApiService
import com.abone.abonex.domain.model.MonthlySpend
import com.abone.abonex.domain.model.Subscription
import com.abone.abonex.domain.repository.SubscriptionRepository
import com.abone.abonex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepositoryImpl @Inject constructor(
    private val api: SubscriptionApiService
) : SubscriptionRepository {
    override fun getUserSubscriptions(): Flow<Resource<List<Subscription>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getUserSubscriptions()
            if(response.isSuccessful && response.body() != null) {
                val subscriptions = response.body()!!.map { it.toSubscription() }
                emit(Resource.Success(subscriptions))
            } else {
                emit(Resource.Error("Abonelikler alınamadı!"))
            }
        }catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu!"))
        }
    }

    override fun getCurrentMonthTotalSpend(): Flow<Resource<MonthlySpend>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getCurrentMonthTotalSpend()
            if (response.isSuccessful && response.body() != null) {
                val monthlySpend = response.body()!!.toMonthlySpend()
                emit(Resource.Success(monthlySpend))
            } else {
                emit(Resource.Error("Aylık harcama bilgisi alınamadı."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu."))
        }
    }
}