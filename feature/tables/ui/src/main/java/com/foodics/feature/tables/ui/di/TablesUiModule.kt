package com.foodics.feature.tables.ui.di

import com.foodics.feature.tables.ui.viewmodel.TablesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val tablesUiModule = module {
    viewModelOf(::TablesViewModel)
}