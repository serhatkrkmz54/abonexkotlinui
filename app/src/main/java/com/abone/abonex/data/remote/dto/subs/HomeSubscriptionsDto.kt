package com.abone.abonex.data.remote.dto.subs

data class HomeSubscriptionsDto(
    val overduePayments: List<SubscriptionDto>?,
    val upcomingPayments: List<SubscriptionDto>?,
    val expiredSubscriptions: List<SubscriptionDto>?,
    val otherSubscriptions: List<SubscriptionDto>?
)