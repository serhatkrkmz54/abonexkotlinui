package com.abone.abonex.data.mapper


import com.abone.abonex.data.remote.dto.subs.CardSpendingDto
import com.abone.abonex.data.remote.dto.subs.DashboardSummaryDto
import com.abone.abonex.data.remote.dto.subs.NextPaymentDto
import com.abone.abonex.data.remote.dto.subs.SimpleSubscriptionDto
import com.abone.abonex.domain.model.CardSpending
import com.abone.abonex.domain.model.DashboardSummary
import com.abone.abonex.domain.model.NextPayment
import com.abone.abonex.domain.model.SimpleSubscription
import java.time.LocalDate

fun SimpleSubscriptionDto.toDomain(): SimpleSubscription {
    return SimpleSubscription(
        name = this.name,
        monthlyCost = this.monthlyCost
    )
}

fun NextPaymentDto.toDomain(): NextPayment {
    return NextPayment(
        name = this.name,
        nextPaymentDate = LocalDate.parse(this.nextPaymentDate),
        amount = this.amount
    )
}

fun DashboardSummaryDto.toDomain(): DashboardSummary {
    return DashboardSummary(
        totalMonthlyCost = this.totalMonthlyCost,
        activeSubscriptionCount = this.activeSubscriptionCount,
        mostExpensiveSubscription = this.mostExpensiveSubscription?.toDomain(),
        nextBigPayment = this.nextBigPayment?.toDomain()
    )
}

fun CardSpendingDto.toDomain(): CardSpending {
    return CardSpending(
        cardName = this.cardName,
        cardLastFourDigits = this.cardLastFourDigits,
        totalAmount = this.totalAmount
    )
}