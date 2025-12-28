package com.farshidabz.skysense.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import platform.Foundation.NSLog

private object IosKtorLogger : Logger {
    override fun log(message: String) {
        NSLog("Ktor: %@", message)
    }
}

actual fun provideHttpClient(config: NetworkConfig): HttpClient {
    return HttpClient(Darwin) {
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
            logger = IosKtorLogger
            level = LogLevel.BODY
            sanitizeHeader { header ->
                header == HttpHeaders.Authorization || header == HttpHeaders.Cookie
            }
        }
    }
}
