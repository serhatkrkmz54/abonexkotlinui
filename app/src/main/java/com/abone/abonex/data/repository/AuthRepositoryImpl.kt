package com.abone.abonex.data.repository

import com.abone.abonex.data.remote.api.AuthApiService
import com.abone.abonex.data.remote.dto.ApiErrorResponse
import com.abone.abonex.data.remote.dto.AuthResponse
import com.abone.abonex.data.remote.dto.LoginRequest
import com.abone.abonex.data.remote.dto.ReactivateRequest
import com.abone.abonex.data.remote.dto.RegisterRequest
import com.abone.abonex.data.remote.dto.UserDto
import com.abone.abonex.domain.repository.AuthRepository
import com.abone.abonex.util.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Resource<AuthResponse> {
        return try {
            val response = apiService.login(request)
            if (response.isSuccessful) {
                Resource.Success(response.body()!!)
            } else {
                var errorMessage: String? = null
                val errorBody = response.errorBody()?.string()

                if (errorBody != null) {
                    try {
                        val type = object : TypeToken<Map<String, String>>() {}.type
                        val errorMap: Map<String, String> = Gson().fromJson(errorBody, type)

                        errorMessage = errorMap.values.firstOrNull()
                    } catch (e: Exception) {
                        errorMessage = "Hata (Kod: ${response.code()})"
                    }
                }
                Resource.Error(errorMessage ?: "Bir hata oluştu (Kod: ${response.code()})")
            }
        } catch (e: Exception) {
            Resource.Error("Bir hata oluştu: ${e.message}")
        }
    }

    override suspend fun register(request: RegisterRequest): Resource<AuthResponse> {
        return try {
            val response = apiService.register(request)
            if (response.isSuccessful) {
                Resource.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    val type = object : TypeToken<Map<String, String>>() {}.type
                    val errorMap: Map<String, String> = Gson().fromJson(errorBody, type)
                    errorMap.values.firstOrNull()
                } catch (e: Exception) {
                    "Kayıt başarısız oldu (Kod: ${response.code()})"
                }
                Resource.Error(errorMessage ?: "Bilinmeyen bir hata oluştu (Kod: ${response.code()})")
            }
        } catch (e: Exception) {
            Resource.Error("Bir hata oluştu: ${e.message}")
        }
    }

    override suspend fun reactivateAccount(request: ReactivateRequest): Resource<AuthResponse> {
        TODO("Not yet implemented")
    }

}