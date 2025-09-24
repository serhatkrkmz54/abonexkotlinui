package com.abone.abonex.domain.model

import java.time.LocalDateTime

data class Notification(
    val id: Long,
    val title: String,
    val body: String,
    val isRead: Boolean,
    val createdAt: LocalDateTime,
    val relatedSubscriptionId: Long?
)