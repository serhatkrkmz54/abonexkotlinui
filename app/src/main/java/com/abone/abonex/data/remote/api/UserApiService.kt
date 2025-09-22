package com.abone.abonex.data.remote.api

import com.abone.abonex.data.remote.dto.UpdateFcmTokenRequest
import com.abone.abonex.data.remote.dto.UserDto
import com.abone.abonex.data.remote.dto.UserProfileUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserApiService {
    @GET("api/user/profile")
    suspend fun getUserProfile(): Response<UserDto>

    @PATCH("api/user/update-profile")
    suspend fun updateUserProfile(@Body request: UserProfileUpdateRequest): Response<UserDto>

    @PATCH("api/user/deactive-account")
    suspend fun deactivateAccount(): Response<Unit>

    @POST("api/user/fcm-token")
    suspend fun updateFcmToken(@Body request: UpdateFcmTokenRequest): Response<Unit>

}
