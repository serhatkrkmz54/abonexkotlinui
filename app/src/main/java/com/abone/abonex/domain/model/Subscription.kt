package com.abone.abonex.domain.model

data class Subscription(
    val id: Long,
    val name: String,
    val amount: Double,
    val currency: String,
    val nextPaymentDate: String
)