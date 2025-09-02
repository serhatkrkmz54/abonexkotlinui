package com.abone.abonex.data.remote.dto.subs

data class MonthlySpendDto(
    val totalAmountSpent: Double,
    val currency: String,
    val month: String,
    val year: Int
)