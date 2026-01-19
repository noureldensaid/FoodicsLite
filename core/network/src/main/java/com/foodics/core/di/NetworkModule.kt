package com.foodics.core.di

import com.foodics.core.network.HttpClientFactory
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    singleOf(::HttpClientFactory)
    single<HttpClient>(createdAtStart = true) { get<HttpClientFactory>().build() }
}