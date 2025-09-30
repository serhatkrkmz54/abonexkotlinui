package com.abone.abonex.domain.model

import java.math.BigDecimal

data class CardSpending(
    val cardName: String,
    val cardLastFourDigits: String,
    val totalAmount: BigDecimal
)