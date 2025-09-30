package com.abone.abonex.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class DashboardSummary(
    val totalMonthlyCost: BigDecimal,
    val activeSubscriptionCount: Long,
    val mostExpensiveSubscription: SimpleSubscription?,
    val nextBigPayment: NextPayment?
)

data class SimpleSubscription(
    val name: String,
    val monthlyCost: BigDecimal
)

data class NextPayment(
    val name: String,
    val nextPaymentDate: LocalDate,
    val amount: BigDecimal
)