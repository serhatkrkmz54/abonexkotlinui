package com.abone.abonex.data.remote.dto.subs

import java.math.BigDecimal

data class CardSpendingDto(
    val cardName: String,
    val cardLastFourDigits: String,
    val totalAmount: BigDecimal
)
