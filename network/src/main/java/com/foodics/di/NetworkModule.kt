package com.foodics.di

import com.foodics.network.HttpClientFactory
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    singleOf(::HttpClientFactory)
    single<HttpClient>(createdAtStart = true) { get<HttpClientFactory>().build() }
}