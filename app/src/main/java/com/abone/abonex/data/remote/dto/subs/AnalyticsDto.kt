package com.abone.abonex.data.remote.dto.subs

import java.math.BigDecimal
import java.time.LocalDate

data class DashboardSummaryDto(
    val totalMonthlyCost: BigDecimal,
    val activeSubscriptionCount: Long,
    val mostExpensiveSubscription: SimpleSubscriptionDto?,
    val nextBigPayment: NextPaymentDto?
)

data class SimpleSubscriptionDto(
    val name: String,
    val monthlyCost: BigDecimal
)

data class NextPaymentDto(
    val name: String,
    val nextPaymentDate: String,
    val amount: BigDecimal
)