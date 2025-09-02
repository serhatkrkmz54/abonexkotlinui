package com.abone.abonex.data.mapper

import com.abone.abonex.data.remote.dto.subs.MonthlySpendDto
import com.abone.abonex.data.remote.dto.subs.SubscriptionDto
import com.abone.abonex.domain.model.MonthlySpend
import com.abone.abonex.domain.model.Subscription
import java.time.Month

fun SubscriptionDto.toSubscription(): Subscription {
    return Subscription(
        id = id,
        name = subscriptionName,
        amount = amount,
        currency = currency,
        nextPaymentDate = nextPaymentDate
    )
}

fun MonthlySpendDto.toMonthlySpend(): MonthlySpend {
    return MonthlySpend(
        totalAmount = totalAmountSpent,
        currency = currency,
        month = Month.valueOf(month),
        year = year
    )
}