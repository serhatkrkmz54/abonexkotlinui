package com.abone.abonex.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentHistory(
    val id: Long,
    val paymentDate: LocalDateTime,
    val amountPaid: BigDecimal,
    val currency: String
)