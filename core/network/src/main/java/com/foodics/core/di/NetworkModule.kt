package com.foodics.core.di

import com.foodics.core.network.HttpClientFactory
import com.foodics.core.util.connectivityObserver.AndroidConnectivityObserver
import com.foodics.core.util.connectivityObserver.ConnectivityObserver
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {

    single { HttpClientFactory(androidContext()) }

    single<HttpClient>(createdAtStart = true) {
        get<HttpClientFactory>().build()
    }

    singleOf(::AndroidConnectivityObserver) { bind<ConnectivityObserver>() }

}