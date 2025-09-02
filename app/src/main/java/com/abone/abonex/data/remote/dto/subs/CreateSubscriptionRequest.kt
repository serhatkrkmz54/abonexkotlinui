package com.abone.abonex.data.remote.dto.subs

data class CreateSubscriptionRequest(
    val subscriptionName: String,
    val amount: Double,
    val currency: String,
    val billingCycle: String,
    val startDate: String,
    val endDate: String?,
    val cardName: String?,
    val cardLastFourDigits: String?,
    val notificationDaysBefore: Int
)