package com.foodics.core.di

import com.foodics.core.network.HttpClientFactory
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {

    single { HttpClientFactory(androidContext()) }

    single<HttpClient>(createdAtStart = true) {
        get<HttpClientFactory>().build()
    }
}