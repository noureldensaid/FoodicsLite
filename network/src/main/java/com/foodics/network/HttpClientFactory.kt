package com.foodics.network

import android.content.Context
import android.os.Build
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import timber.log.Timber
import java.util.concurrent.TimeUnit


private const val DEFAULT_TIMEOUT_SECONDS = 60L

class HttpClientFactory(
    private val context: Context,
) {

    private var baseUrl: String = ""

    private val okhttpEngine by lazy {
        OkHttp.create {
            config {
                connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                connectionPool(ConnectionPool(5, 30, TimeUnit.SECONDS))
                if (BuildConfig.DEBUG) {
                    addInterceptor(
                        ChuckerInterceptor.Builder(context)
                            .collector(ChuckerCollector(context))
                            .createShortcut(false)
                            .build()
                    )
                }
            }
        }
    }

    private val userAgentContent by lazy {
        "${context.getString(R.string.app_name)}/${BuildConfig.DEBUG} " +
                "(Build Android ${Build.VERSION.RELEASE}; " + "${Build.MANUFACTURER} ${Build.MODEL})"
    }

    fun build(): HttpClient {
        return HttpClient(okhttpEngine) {

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = BuildConfig.DEBUG
                    encodeDefaults = true
                })
            }

            install(HttpCookies) {
                storage = AcceptAllCookiesStorage()
            }

            if (BuildConfig.DEBUG) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Timber.tag("HttpClient").d(message)
                        }
                    }
                    level = LogLevel.ALL
                }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = DEFAULT_TIMEOUT_SECONDS * 1000
                connectTimeoutMillis = DEFAULT_TIMEOUT_SECONDS * 1000
                socketTimeoutMillis = DEFAULT_TIMEOUT_SECONDS * 1000
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                userAgent(userAgentContent)
                url(baseUrl)
            }
        }
    }
}