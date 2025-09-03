package com.abone.abonex.data.remote.dto.subs

data class CreateSubscriptionFromPlanRequest(
    val planId: Long,
    val startDate: String,
    val endDate: String?,
    val cardName: String?,
    val cardLastFourDigits: String?,
    val notificationDaysBefore: Int,
    val firstPaymentMade: Boolean
)