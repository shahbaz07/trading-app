package com.sss.feature.stock.di

import com.sss.feature.stock.data.remote.FakeStockService
import com.sss.feature.stock.data.remote.StockService
import com.sss.feature.stock.data.repository.DefaultStockRepository
import com.sss.feature.stock.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StockModule {

    @Binds
    @Singleton
    abstract fun bindStockRepository(impl: DefaultStockRepository): StockRepository

    @Binds
    @Singleton
    abstract fun bindStockService(impl: FakeStockService): StockService
}
