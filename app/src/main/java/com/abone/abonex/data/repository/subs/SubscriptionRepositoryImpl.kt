package com.abone.abonex.data.repository.subs

import android.util.Log
import com.abone.abonex.data.mapper.toHomeSubscriptions
import com.abone.abonex.data.mapper.toMonthlySpend
import com.abone.abonex.data.mapper.toSubscription
import com.abone.abonex.data.mapper.toSubscriptionDetail
import com.abone.abonex.data.mapper.toTemplate
import com.abone.abonex.data.remote.api.subs.SubscriptionApiService
import com.abone.abonex.data.remote.dto.subs.CreateSubscriptionFromPlanRequest
import com.abone.abonex.data.remote.dto.subs.CreateSubscriptionRequest
import com.abone.abonex.domain.model.HomeSubscriptions
import com.abone.abonex.domain.model.MonthlySpend
import com.abone.abonex.domain.model.Subscription
import com.abone.abonex.domain.model.SubscriptionDetail
import com.abone.abonex.domain.model.Template
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

    private val _homeSubscriptionsFlow = MutableSharedFlow<Resource<HomeSubscriptions>>(replay = 1)
    private val _monthlySpendFlow = MutableSharedFlow<Resource<MonthlySpend>>(replay = 1)

    override fun getHomeViewSubscriptions(): Flow<Resource<HomeSubscriptions>> = _homeSubscriptionsFlow
    override fun getCurrentMonthTotalSpend(): Flow<Resource<MonthlySpend>> = _monthlySpendFlow

    override fun getTemplates(): Flow<Resource<List<Template>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getAllTemplates()
            if (response.isSuccessful && response.body() != null) {
                val templates = response.body()!!.map { it.toTemplate() }
                emit(Resource.Success(templates))
            } else {
                emit(Resource.Error("Şablonlar alınamadı: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu."))
        }
    }

    override fun getSubscriptionDetails(id: Long): Flow<Resource<SubscriptionDetail>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getSubscriptionDetails(id)
            if (response.isSuccessful && response.body() != null) {
                val detailModel = response.body()!!.toSubscriptionDetail()
                emit(Resource.Success(detailModel))
            } else {
                emit(Resource.Error("Detaylar alınamadı: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu."))
        }
    }

    override suspend fun refreshHomeViewSubscriptions() {
        _homeSubscriptionsFlow.emit(Resource.Loading())
        try {
            val response = api.getHomeViewSubscriptions()
            if (response.isSuccessful && response.body() != null) {
                val homeSubscriptions = response.body()!!.toHomeSubscriptions()
                _homeSubscriptionsFlow.emit(Resource.Success(homeSubscriptions))
            } else {
                _homeSubscriptionsFlow.emit(Resource.Error("Abonelikler alınamadı."))
            }
        } catch (e: Exception) {
            _homeSubscriptionsFlow.emit(Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu."))
        }
    }

    override suspend fun refreshMonthlySpend() {
        try {
            val response = api.getCurrentMonthTotalSpend()
            if (response.isSuccessful && response.body() != null) {
                val monthlySpend = response.body()!!.toMonthlySpend()
                _monthlySpendFlow.emit(Resource.Success(monthlySpend))
            } else {
            }
        } catch (e: Exception) {
        }
    }

    override suspend fun createSubscriptionFromPlan(request: CreateSubscriptionFromPlanRequest): Resource<Subscription> {
        val result = try {
            val response = api.createSubscriptionFromPlan(request)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toSubscription())
            } else {
                Resource.Error("Abonelik oluşturulamadı: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu.")
        }

        if (result is Resource.Success) {
            refreshHomeViewSubscriptions()
            refreshMonthlySpend()
        }
        return result
    }

    override suspend fun logPayment(id: Long): Resource<Unit> {
        return try {
            val response = api.logPayment(id)
            if (response.isSuccessful) {
                refreshHomeViewSubscriptions()
                refreshMonthlySpend()
                Resource.Success(Unit)
            } else {
                Resource.Error("Ödeme kaydedilemedi: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu.")
        }
    }

    override suspend fun cancelSubscription(id: Long): Resource<Unit> {
        return try {
            val response = api.cancelSubscription(id)
            if (response.isSuccessful) {
                refreshHomeViewSubscriptions()
                refreshMonthlySpend()
                Resource.Success(Unit)
            } else {
                Resource.Error("Abonelik iptal edilemedi: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu.")
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
            refreshHomeViewSubscriptions()
            refreshMonthlySpend()
        }

        return result
    }
}