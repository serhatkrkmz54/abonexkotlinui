package com.abone.abonex.domain.model

data class Subscription(
    val id: Long,
    val name: String,
    val amount: Double,
    val currency: String,
    val billingCycle: String,
    val nextPaymentDate: String,
    val startDate: String,
    val endDate: String?,
    val cardName: String?,
    val cardLastFourDigits: String?,
    val logoUrl: String?
)