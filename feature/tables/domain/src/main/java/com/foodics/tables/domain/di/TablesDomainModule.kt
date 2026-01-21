package com.foodics.tables.domain.di

import com.foodics.tables.domain.usecase.AddProductUseCase
import com.foodics.tables.domain.usecase.ClearCartUseCase
import com.foodics.tables.domain.usecase.GetOrderedProductsUseCase
import com.foodics.tables.domain.usecase.ObserveCartSummaryUseCase
import com.foodics.tables.domain.usecase.ObserveCategoriesUseCase
import com.foodics.tables.domain.usecase.ObserveProductsUseCase
import com.foodics.tables.domain.usecase.SyncCategoriesUseCase
import com.foodics.tables.domain.usecase.SyncProductsForCategoryUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val tablesDomainModule = module {

    factoryOf(::ObserveCategoriesUseCase)
    factoryOf(::ObserveProductsUseCase)
    factoryOf(::ObserveCartSummaryUseCase)

    factoryOf(::SyncCategoriesUseCase)
    factoryOf(::SyncProductsForCategoryUseCase)

    factoryOf(::AddProductUseCase)
    factoryOf(::ClearCartUseCase)
    factoryOf(::GetOrderedProductsUseCase)
}