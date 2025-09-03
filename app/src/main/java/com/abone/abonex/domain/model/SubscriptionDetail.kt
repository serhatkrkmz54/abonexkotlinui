package com.abone.abonex.domain.model

data class SubscriptionDetail(
    val subscription: Subscription,
    val paymentHistory: List<PaymentHistory>
)