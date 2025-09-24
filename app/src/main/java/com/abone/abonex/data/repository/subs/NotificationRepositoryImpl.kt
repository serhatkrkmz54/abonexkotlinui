package com.abone.abonex.data.repository.subs

import com.abone.abonex.data.mapper.toNotification
import com.abone.abonex.data.remote.api.subs.NotificationApiService
import com.abone.abonex.domain.model.Notification
import com.abone.abonex.domain.repository.NotificationRepository
import com.abone.abonex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationApiService
) : NotificationRepository {

    private val _notificationsFlow = MutableSharedFlow<Resource<List<Notification>>>(replay = 1)
    private val _unreadCountFlow = MutableSharedFlow<Resource<Int>>(replay = 1)

    override fun getNotifications(): Flow<Resource<List<Notification>>> = _notificationsFlow
    override fun getUnreadCount(): Flow<Resource<Int>> = _unreadCountFlow

    override suspend fun refreshNotifications() {
        _notificationsFlow.emit(Resource.Loading())
        try {
            val response = api.getNotifications()
            if (response.isSuccessful && response.body() != null) {
                _notificationsFlow.emit(Resource.Success(response.body()!!.map { it.toNotification() }))
            } else {
                _notificationsFlow.emit(Resource.Error("Bildirimler alınamadı."))
            }
        } catch (e: Exception) {
            _notificationsFlow.emit(Resource.Error(e.message))
        }
    }

    override suspend fun refreshUnreadCount() {
        try {
            val response = api.getUnreadCount()
            if (response.isSuccessful && response.body() != null) {
                _unreadCountFlow.emit(Resource.Success(response.body()!!.unreadCount))
            }
        } catch (e: Exception) { /* Hata yönetimi */ }
    }

    override suspend fun markAsRead(id: Long): Resource<Unit> {
        return try {
            if (api.markAsRead(id).isSuccessful) {
                refreshNotifications()
                refreshUnreadCount()
                Resource.Success(Unit)
            } else { Resource.Error("Okundu olarak işaretlenemedi.") }
        } catch (e: Exception) { Resource.Error(e.message) }
    }

    override suspend fun markAllAsRead(): Resource<Unit> {
        return try {
            if (api.markAllAsRead().isSuccessful) {
                refreshNotifications()
                refreshUnreadCount()
                Resource.Success(Unit)
            } else { Resource.Error("Tümü okundu olarak işaretlenemedi.") }
        } catch (e: Exception) { Resource.Error(e.message) }
    }
}