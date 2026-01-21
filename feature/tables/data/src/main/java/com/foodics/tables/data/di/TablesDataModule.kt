package com.foodics.tables.data.di

import com.foodics.tables.data.mapper.CartSummaryDbToDomainMapper
import com.foodics.tables.data.mapper.CategoryEntityToDomainMapper
import com.foodics.tables.data.mapper.CategoryRemoteToEntityMapper
import com.foodics.tables.data.mapper.ProductEntityToDomainMapper
import com.foodics.tables.data.remote.TablesRemoteDataSource
import com.foodics.tables.data.remote.TablesRemoteDataSourceImpl
import com.foodics.tables.data.repository.TablesRepositoryImpl
import com.foodics.tables.domain.repository.TablesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val tablesDataModule = module {

    // Remote (needs HttpClient from networkModule)
    singleOf(::TablesRemoteDataSourceImpl) { bind<TablesRemoteDataSource>() }

    // Mappers
    factoryOf(::CategoryRemoteToEntityMapper)
    factoryOf(::CategoryEntityToDomainMapper)
    factoryOf(::ProductEntityToDomainMapper)
    factoryOf(::CartSummaryDbToDomainMapper)

    // Repository
    singleOf(::TablesRepositoryImpl) { bind<TablesRepository>() }
}