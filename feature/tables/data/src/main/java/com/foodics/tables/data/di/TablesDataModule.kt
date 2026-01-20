package com.foodics.tables.data.di

import com.foodics.tables.data.mapper.CartSummaryDbToDomainMapper
import com.foodics.tables.data.mapper.CategoryEntityToDomainMapper
import com.foodics.tables.data.mapper.CategoryRemoteToEntityMapper
import com.foodics.tables.data.mapper.ProductEntityToDomainMapper
import com.foodics.tables.data.remote.TablesRemoteDataSource
import com.foodics.tables.data.remote.TablesRemoteDataSourceImpl
import com.foodics.tables.data.repository.TablesRepositoryImpl
import com.foodics.tables.domain.repository.TablesRepository
import org.koin.dsl.module

val tablesDataModule = module {

    single<TablesRemoteDataSource> { TablesRemoteDataSourceImpl(get()) } // HttpClient

    factory { CategoryRemoteToEntityMapper() }
    factory { CategoryEntityToDomainMapper() }
    factory { ProductEntityToDomainMapper() }
    factory { CartSummaryDbToDomainMapper() }

    single<TablesRepository> {
        TablesRepositoryImpl(
            db = get(),
            categoryDao = get(),
            productDao = get(),
            remote = get(),
            categoryRemoteToEntity = get(),
            categoryEntityToDomain = get(),
            productEntityToDomain = get(),
            cartSummaryDbToDomain = get()
        )
    }
}