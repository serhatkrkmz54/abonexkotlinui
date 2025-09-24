package com.abone.abonex.data.repository

import android.util.Log
import com.abone.abonex.data.local.TokenManager
import com.abone.abonex.data.remote.api.AuthApiService
import com.abone.abonex.data.remote.dto.ApiErrorResponse
import com.abone.abonex.data.remote.dto.AuthResponse
import com.abone.abonex.data.remote.dto.LoginRequest
import com.abone.abonex.data.remote.dto.ReactivateRequest
import com.abone.abonex.data.remote.dto.RegisterRequest
import com.abone.abonex.data.remote.dto.UserDto
import com.abone.abonex.data.remote.dto.VerifyCodeRequest
import com.abone.abonex.domain.repository.AuthRepository
import com.abone.abonex.domain.repository.UserRepository
import com.abone.abonex.util.Resource
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Resource<AuthResponse> {
        return try {
            val response = apiService.login(request)
            if (response.isSuccessful && response.body() != null) {
                val token = response.body()!!.token
                tokenManager.saveToken(token)
                sendFcmTokenToServer()
                Resource.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                if (errorBody != null && errorBody.contains("HESAP_PASIF")) {
                    Resource.Error("HESAP_PASIF")
                }else {
                    val errorMessage = parseErrorMessage(errorBody, response.code())
                    Resource.Error(errorMessage)
                }
            }
        } catch (e: Exception) {
            Resource.Error("Bir hata oluştu: ${e.message}")
        }
    }

    override suspend fun register(request: RegisterRequest): Resource<AuthResponse> {
        return try {
            val response = apiService.register(request)
            if (response.isSuccessful && response.body() != null) {
                sendFcmTokenToServer()
                Resource.Success(response.body()!!)
            }else {
                Resource.Error(parseErrorMessage(response.errorBody()?.string(), response.code()))
            }
        } catch (e: Exception) {
            Resource.Error("Bir hata oluştu: ${e.message}")
        }
    }

    override suspend fun requestReactivationOtp(request: ReactivateRequest): Resource<Unit> {
        return try {
            val response = apiService.requestReactivationOtp(request)
            if (response.isSuccessful) Resource.Success(Unit)
            else Resource.Error(parseErrorMessage(response.errorBody()?.string(), response.code()))
        } catch (e: Exception) { Resource.Error(e.message) }
    }

    override suspend fun verifyReactivationOtp(request: VerifyCodeRequest): Resource<AuthResponse> {
        return try {
            val response = apiService.verifyReactivationOtp(request)
            if (response.isSuccessful && response.body() != null) {
                sendFcmTokenToServer()
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(parseErrorMessage(response.errorBody()?.string(), response.code()))
            }
        } catch (e: Exception) { Resource.Error(e.message) }
    }

    private suspend fun sendFcmTokenToServer() {
        try {
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            Log.d("AuthRepo", "FCM Token alındı, sunucuya gönderiliyor: $fcmToken")
            userRepository.updateFcmToken(fcmToken)
        } catch (e: Exception) {
            Log.e("AuthRepo", "FCM token alınamadı veya gönderilemedi", e)
        }
    }

    private fun parseErrorMessage(errorBody: String?, statusCode: Int): String {
        if (errorBody==null) return "Bir hata oluştu (Kod:$statusCode)"
        return try {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val errorMap: Map<String, Any> = Gson().fromJson(errorBody,type)
            errorMap["message"]?.toString() ?: errorMap["error"]?.toString() ?: "Sunucu hatası (Kod: $statusCode)"
        }catch (e: Exception) {
            "Hata (Kod: $statusCode)"
        }
    }
}