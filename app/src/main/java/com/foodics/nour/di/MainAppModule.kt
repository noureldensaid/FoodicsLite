package com.foodics.nour.di

import com.foodics.nour.ui.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val mainAppModule = module {
    viewModelOf(::MainViewModel)
}