package com.abone.abonex.domain.model

data class HomeSubscriptions(
    val overdue: List<Subscription>,
    val upcoming: List<Subscription>,
    val expired: List<Subscription>,
    val other: List<Subscription>
)