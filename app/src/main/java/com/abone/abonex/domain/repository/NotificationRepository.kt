package com.abone.abonex.domain.repository

import com.abone.abonex.domain.model.Notification
import com.abone.abonex.util.Resource
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<Resource<List<Notification>>>
    fun getUnreadCount(): Flow<Resource<Int>>
    suspend fun refreshNotifications()
    suspend fun refreshUnreadCount()
    suspend fun markAsRead(id: Long): Resource<Unit>
    suspend fun markAllAsRead(): Resource<Unit>
}