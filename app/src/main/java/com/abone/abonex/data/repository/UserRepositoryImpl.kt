package com.abone.abonex.data.repository

import com.abone.abonex.data.remote.api.UserApiService
import com.abone.abonex.data.remote.dto.UserDto
import com.abone.abonex.data.remote.dto.UserProfileUpdateRequest
import com.abone.abonex.domain.repository.UserRepository
import com.abone.abonex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRepository {

    private val userProfileFlow = MutableSharedFlow<UserDto>(replay = 1)

    override fun getCachedUserProfile(): Flow<UserDto> = userProfileFlow

    override suspend fun loadUserProfile(): Resource<Unit> {
        return try {
            val response = userApiService.getUserProfile()
            if (response.isSuccessful && response.body() != null) {
                userProfileFlow.emit(response.body()!!)
                Resource.Success(Unit)
            } else {
                Resource.Error("Profil bilgileri alınamadı.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu.")
        }
    }

    override suspend fun updateUserProfile(request: UserProfileUpdateRequest): Resource<UserDto> {
        return try {
            val response = userApiService.updateUserProfile(request)
            if (response.isSuccessful && response.body() != null) {
                val updatedUser = response.body()!!
                userProfileFlow.emit(updatedUser)
                Resource.Success(updatedUser)
            } else {
                Resource.Error("Profil güncellenemedi.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu.")
        }
    }

    override suspend fun deactivateAccount(): Resource<Unit> {
        return try {
            val response = userApiService.deactivateAccount()
            if (response.isSuccessful) {
                loadUserProfile()
                Resource.Success(Unit)
            } else {
                Resource.Error("Hesap pasif hale getirilemedi.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu.")
        }
    }
}
