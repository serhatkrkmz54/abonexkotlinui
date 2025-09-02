package com.abone.abonex.data.remote.dto.subs

data class SubscriptionDto(
    val id: Long,
    val subscriptionName: String,
    val amount: Double,
    val currency: String,
    val billingCycle: String,
    val startDate: String,
    val nextPaymentDate: String,
    val cardName: String?,
    val cardLastFourDigits: String?,
    val isActive: Boolean
)