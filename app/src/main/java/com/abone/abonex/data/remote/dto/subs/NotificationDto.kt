package com.abone.abonex.data.remote.dto.subs

data class NotificationDto(
    val id: Long,
    val title: String,
    val body: String,
    val isRead: Boolean,
    val type: String,
    val relatedSubscriptionId: Long?,
    val createdAt: String
)

data class UnreadCountDto(
    val unreadCount: Int
)