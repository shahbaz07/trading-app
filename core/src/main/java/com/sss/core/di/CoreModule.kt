package com.sss.core.di

import com.sss.core.network.OkHttpWebSocketClient
import com.sss.core.network.WebSocketClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    @Singleton
    abstract fun bindWebSocketClient(impl: OkHttpWebSocketClient): WebSocketClient
}
