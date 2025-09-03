package com.abone.abonex.data.remote.dto.subs

import java.math.BigDecimal

data class PaymentHistoryDto(
    val id: Long,
    val subscriptionId: Long,
    val subscriptionName: String,
    val paymentDate: String,
    val amountPaid: BigDecimal
)