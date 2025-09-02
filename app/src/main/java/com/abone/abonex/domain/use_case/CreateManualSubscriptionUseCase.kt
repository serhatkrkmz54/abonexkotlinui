package com.abone.abonex.domain.use_case

import com.abone.abonex.data.remote.dto.subs.CreateSubscriptionRequest
import com.abone.abonex.domain.model.Subscription
import com.abone.abonex.domain.repository.SubscriptionRepository
import com.abone.abonex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateManualSubscriptionUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    operator fun invoke(request: CreateSubscriptionRequest): Flow<Resource<Subscription>> = flow {
        emit(Resource.Loading())
        val result = repository.createManualSubscription(request)
        emit(result)
    }
}