package com.abone.abonex.data.mapper

import com.abone.abonex.data.remote.dto.subs.HomeSubscriptionsDto
import com.abone.abonex.data.remote.dto.subs.MonthlySpendDto
import com.abone.abonex.data.remote.dto.subs.PaymentHistoryDto
import com.abone.abonex.data.remote.dto.subs.SubscriptionDetailDto
import com.abone.abonex.data.remote.dto.subs.SubscriptionDto
import com.abone.abonex.domain.model.HomeSubscriptions
import com.abone.abonex.domain.model.MonthlySpend
import com.abone.abonex.domain.model.PaymentHistory
import com.abone.abonex.domain.model.Subscription
import com.abone.abonex.domain.model.SubscriptionDetail
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter

fun SubscriptionDto.toSubscription(): Subscription {
    return Subscription(
        id = id,
        name = subscriptionName,
        amount = amount,
        currency = currency,
        billingCycle = billingCycle,
        nextPaymentDate = nextPaymentDate,
        startDate = startDate,
        endDate = endDate,
        cardName = cardName,
        cardLastFourDigits = cardLastFourDigits,
        logoUrl = logoUrl
    )
}

fun PaymentHistoryDto.toPaymentHistory(currency: String): PaymentHistory {
    return PaymentHistory(
        id = id,
        paymentDate = LocalDateTime.parse(paymentDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        amountPaid = amountPaid,
        currency = currency
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
fun HomeSubscriptionsDto.toHomeSubscriptions(): HomeSubscriptions {
    return HomeSubscriptions(
        overdue = overdueSubscriptions?.map { it.toSubscription() } ?: emptyList(),
        upcoming = upcomingPayments?.map { it.toSubscription() } ?: emptyList(),
        expired = expiredSubscriptions?.map { it.toSubscription() } ?: emptyList(),
        other = otherSubscriptions?.map { it.toSubscription() } ?: emptyList()
    )
}

fun SubscriptionDetailDto.toSubscriptionDetail(): SubscriptionDetail {
    val subscriptionModel = subscription.toSubscription()

    val paymentHistoryModel = paymentHistory.map { historyDto ->
        historyDto.toPaymentHistory(subscriptionModel.currency)
    }

    return SubscriptionDetail(
        subscription = subscriptionModel,
        paymentHistory = paymentHistoryModel
    )
}