package com.abone.abonex.data.remote.api.subs

import com.abone.abonex.data.remote.dto.subs.NotificationDto
import com.abone.abonex.data.remote.dto.subs.UnreadCountDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationApiService {

    @GET("api/v1/notifications")
    suspend fun getNotifications(): Response<List<NotificationDto>>

    @GET("api/v1/notifications/unread-count")
    suspend fun getUnreadCount(): Response<UnreadCountDto>

    @POST("api/v1/notifications/{id}/mark-as-read")
    suspend fun markAsRead(@Path("id") id: Long): Response<Unit>

    @POST("api/v1/notifications/mark-all-as-read")
    suspend fun markAllAsRead(): Response<Unit>
}