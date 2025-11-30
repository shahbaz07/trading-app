package com.sss.tradingapp.di

import com.sss.tradingapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("websocket_url")
    fun provideWebSocketUrl(): String = BuildConfig.WEBSOCKET_URL
}
