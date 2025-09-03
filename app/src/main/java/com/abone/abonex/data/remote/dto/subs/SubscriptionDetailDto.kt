package com.abone.abonex.data.remote.dto.subs

data class SubscriptionDetailDto(
    val subscription: SubscriptionDto,
    val paymentHistory: List<PaymentHistoryDto>
)