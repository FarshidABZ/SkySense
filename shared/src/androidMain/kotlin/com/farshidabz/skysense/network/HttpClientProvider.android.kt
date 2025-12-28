package com.farshidabz.skysense.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private object AndroidKtorLogger : Logger {
    override fun log(message: String) {
        Log.d("Ktor", message)
    }
}

actual fun provideHttpClient(config: NetworkConfig): HttpClient {
    return HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    explicitNulls = false
                }
            )
        }
        install(HttpTimeout) {
            connectTimeoutMillis = config.connectTimeoutMillis
            socketTimeoutMillis = config.socketTimeoutMillis
            requestTimeoutMillis = config.socketTimeoutMillis
        }
        install(Logging) {
            logger = AndroidKtorLogger
            level = LogLevel.BODY
            sanitizeHeader { header ->
                header == HttpHeaders.Authorization || header == HttpHeaders.Cookie
            }
        }
    }
}
