package com.foodics.tables.domain.di

import com.foodics.tables.domain.usecase.AddProductUseCase
import com.foodics.tables.domain.usecase.ClearCartUseCase
import com.foodics.tables.domain.usecase.GetOrderedProductsUseCase
import com.foodics.tables.domain.usecase.ObserveCartSummaryUseCase
import com.foodics.tables.domain.usecase.ObserveCategoriesUseCase
import com.foodics.tables.domain.usecase.ObserveProductsUseCase
import com.foodics.tables.domain.usecase.SyncCategoriesUseCase
import com.foodics.tables.domain.usecase.SyncProductsForCategoryUseCase
import org.koin.dsl.module

val tablesDomainModule = module {

    factory { ObserveCategoriesUseCase(get()) }
    factory { ObserveProductsUseCase(get()) }
    factory { ObserveCartSummaryUseCase(get()) }

    factory { SyncCategoriesUseCase(get()) }
    factory { SyncProductsForCategoryUseCase(get()) }

    factory { AddProductUseCase(get()) }
    factory { ClearCartUseCase(get()) }
    factory { GetOrderedProductsUseCase(get()) }
}