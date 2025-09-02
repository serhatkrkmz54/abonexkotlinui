package com.abone.abonex.data.repository.subs

import android.util.Log
import com.abone.abonex.data.mapper.toMonthlySpend
import com.abone.abonex.data.mapper.toSubscription
import com.abone.abonex.data.remote.api.subs.SubscriptionApiService
import com.abone.abonex.data.remote.dto.subs.CreateSubscriptionRequest
import com.abone.abonex.domain.model.MonthlySpend
import com.abone.abonex.domain.model.Subscription
import com.abone.abonex.domain.repository.SubscriptionRepository
import com.abone.abonex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepositoryImpl @Inject constructor(
    private val api: SubscriptionApiService
) : SubscriptionRepository {

    private val _subscriptionsFlow = MutableSharedFlow<Resource<List<Subscription>>>(replay = 1)
    private val _monthlySpendFlow = MutableSharedFlow<Resource<MonthlySpend>>(replay = 1)

    override fun getUserSubscriptions(): Flow<Resource<List<Subscription>>> = _subscriptionsFlow
    override fun getCurrentMonthTotalSpend(): Flow<Resource<MonthlySpend>> = _monthlySpendFlow

    override suspend fun refreshUserSubscriptions() {
        _subscriptionsFlow.emit(Resource.Loading())
        try {
            val response = api.getUserSubscriptions()
            if (response.isSuccessful && response.body() != null) {
                val subscriptions = response.body()!!.map { it.toSubscription() }
                _subscriptionsFlow.emit(Resource.Success(subscriptions))
            } else {
                _subscriptionsFlow.emit(Resource.Error("Abonelikler alınamadı."))
            }
        } catch (e: Exception) {
            _subscriptionsFlow.emit(Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu."))
        }
    }

    override suspend fun refreshMonthlySpend() {
        try {
            val response = api.getCurrentMonthTotalSpend()
            if (response.isSuccessful && response.body() != null) {
                val monthlySpend = response.body()!!.toMonthlySpend()
                _monthlySpendFlow.emit(Resource.Success(monthlySpend))
            } else {
                // Hata durumunu opsiyonel olarak yayabiliriz
            }
        } catch (e: Exception) {
            // Hata durumunu opsiyonel olarak yayabiliriz
        }
    }

    override suspend fun createManualSubscription(request: CreateSubscriptionRequest): Resource<Subscription> {
        val result = try {
            val response = api.createManualSubscription(request)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toSubscription())
            } else {
                Resource.Error("Abonelik oluşturulamadı: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu.")
        }

        if (result is Resource.Success) {
            refreshUserSubscriptions()
            refreshMonthlySpend()
        }

        return result
    }
}