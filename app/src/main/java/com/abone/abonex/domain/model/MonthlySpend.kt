package com.abone.abonex.domain.model

import java.time.Month

data class MonthlySpend(
    val totalAmount: Double,
    val currency: String,
    val month: Month,
    val year: Int
)