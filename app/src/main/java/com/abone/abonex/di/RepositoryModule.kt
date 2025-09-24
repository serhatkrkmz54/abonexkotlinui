package com.abone.abonex.di

import com.abone.abonex.data.repository.AuthRepositoryImpl
import com.abone.abonex.data.repository.subs.NotificationRepositoryImpl
import com.abone.abonex.domain.repository.AuthRepository
import com.abone.abonex.domain.repository.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository

}