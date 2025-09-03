package com.abone.abonex.data.remote.dto.subs

data class PlanDto(
    val id: Long,
    val planName: String,
    val amount: Double,
    val currency: String,
    val billingCycle: String
)